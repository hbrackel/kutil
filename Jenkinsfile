pipeline {
    agent none

    options {
        skipDefaultCheckout()
        disableConcurrentBuilds()
    }
    environment {
      MAVEN_DEPLOY = credentials('MAVEN_DEPLOY_USER')
    }

  stages {
    stage('Checkout') {
        agent any
        steps {
            checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CleanBeforeCheckout'], [$class: 'RelativeTargetDirectory', relativeTargetDir: 'project']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'SCM_USER', url: 'http://git:3000/util/kutil.git']]])
            checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'gradle-build-scripts']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'SCM_USER', url: 'http://git:3000/cicd/gradle-build-scripts.git']]])
            stash(name: 'ws', includes: 'project/**, gradle-build-files/**', excludes: '**/.git/**')
        }
    }
    stage('Compile & UnitTest') {
        agent {label 'jdk8' }
        steps {
            unstash 'ws'
            gradlew("clean build")
            stash(name: 'build', includes: 'project/build/**')
         }
        post {
            success {
                archiveArtifacts artifacts: 'project/build/libs/**/*.jar,project/build/libs/**/*.txt', fingerprint: true
                junit testResults: 'project/build/reports/**/*.xml', allowEmptyResults: true
            }
        }
    }
    stage('Publish to downstream') {
        agent {label 'jdk8' }
        steps {
            unstash 'ws'
            unstash 'build'
            gradlew("PublishAllPublicationsToDownstreamRepository")
            stash(name: 'build', includes: 'project/build/**')
        }
        post {
            success {
                if (fileExists('project/build/downstream_repo.zip')) {
                    sh "rm project/build/downstream_repo.zip"
                }
                zip zipFile: 'project/build/downstream_repo.zip', archive: true, dir: 'project/build/repo'
            }
        }
    }

    stage('Integration Tests') {
      steps {
        echo 'no integration tests defined'
      }
    }

    stage('Code Analysis') {
      steps {
        echo 'Code Analysis'
        //sh './gradlew sonarqube -Dsonar.login=${SONARQUBE_TOKEN} -Dsonar.host.url=${SONARQUBE_URL}'
      }
    }


    stage('Functional Tests & Performance Tests') {
      parallel {
        stage('FunctionalTests') {
          agent none
          steps {
            echo 'no functional tests defined'
          }
        }
        stage('Performance & Stress Tests') {
          agent none
          steps {
            echo 'no performance or stress tests defined'
          }
        }
      }
    }


    stage('User Acceptance Tests') {
        steps {
            echo 'no UAT defined'
        }
    }

    stage('Publish Library') {
      agent {label 'jdk8' }
      when {
        branch 'master'
      }
      steps {
        gradlew('publishAllPublicationsToExternalRepository')
      }
    }
  }
}

def gradlew(String commands) {
    sh "cd project && ./gradlew --no-daemon --console=plain --refresh-dependencies ${commands}"
}