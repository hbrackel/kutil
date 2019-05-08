pipeline {
    agent none

    options {
        skipDefaultCheckout()
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
        agent { label 'jdk8' }
        steps {
            unstash 'ws'
            gradlew("clean build")
            stash(name: 'build', includes: 'project/build/**')
         }
    }

    stage('Publish to downstream') {
        agent { label 'jdk8' }
        steps {
            unstash 'ws'
            unstash 'build'
            gradlew("PublishAllPublicationsToDownstreamRepository")
            stash(name: 'build', includes: 'project/build/**')
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

    stage('Assemble Distribution') {
      steps {
        echo 'Assemble'
        //sh './gradlew assemble generatePomFileForMavenPublication'
        //archiveArtifacts artifacts: 'build/libs/*.jar, build/publications/**/*pom*.xml', onlyIfSuccessful: true
      }
    }

    stage('Publish Binaries') {
      steps {
        echo 'publish binaries to QA repository'
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

    stage('Deploy Binaries') {
      when {
        branch 'master'
      }
      steps {
        echo 'Deploy into Production'
        //sh 'gradlew publish'
      }
    }
  }
}

def gradlew(String commands) {
    sh "cd project && gradle --no-daemon --console=plain --refresh-dependencies ${commands}"
}