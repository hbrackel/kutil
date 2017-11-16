pipeline {
    agent {
        docker {
          image 'openjdk:8u151-jdk'
        }
    }

    environment {
      MAVEN_DEPLOY = credentials('MAVEN_REPO_DEPLOY_SECRET')
      SONARQUBE_TOKEN = credentials('SONARQUBE_TOKEN')

    }

  stages {
    stage('Compile & Unit Tests') {
      steps {
        echo 'Build'
        sh './gradlew clean classes'
        sh './gradlew check'
        junit 'build/test-results/**/*.xml'
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
        sh './gradlew sonarqube -Dsonar.login=${SONARQUBE_TOKEN} -Dsonar.host.url=${SONARQUBE_URL}'
      }
    }

    stage('Assemble Distribution') {
      steps {
        sh './gradlew assemble generatePomFileForMavenJavaPublication'
        archiveArtifacts artifacts: 'build/libs/*.jar, build/publications/**/*pom*.xml', onlyIfSuccessful: true
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
        sh './gradlew publish'
      }
    }
  }
}