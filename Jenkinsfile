pipeline {
    agent any

    options {
        skipDefaultCheckout=true
    }

    environment {
      MAVEN_DEPLOY = credentials('MAVEN_DEPLOY_USER')
    }

  stages {

    stage('Checkout BuildScripts') {
        steps {
            checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'project']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'SCM_USER', url: 'http://git:3000/util/kutil.git']]])
            checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'gradle-build-scripts']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'SCM_USER', url: 'http://git:3000/cicd/gradle-build-scripts.git']]])
        }
    }
    stage('Compile & Unit Test') {
      steps {
        echo 'Build'
        gradlew("clean build")
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
        //sh './gradlew publish'
      }
    }
  }
}

def gradlew(String commands) {
    sh "cd project && ./gradlew --no-daemon --console=plain ${commands}"
}