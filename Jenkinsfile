pipeline {
    agent {
        label 'jdk17'
    }
    environment {
        MAVEN_DEPLOY = credentials('maven-deploy-user')
    }
  stages {
    stage('Compile & UnitTest') {
        steps {
            gradlew("clean build")
            stash(name: 'build', includes: 'build/**')
         }
        post {
            always {
                junit allowEmptyResults: true, testResults: 'build/test-results/**/*.xml'
            }
        }
    }

    stage('Validation') {
        parallel {
            stage('Integration Tests') {
                steps {
                    echo 'no Integration Tests defined'
                }
            }

            stage('User Acceptance Tests') {
                steps {
                    echo 'no UAT defined'
                }
            }

            stage('Static Application security Testing (SAST)') {
                steps {
                    echo 'no SAST defined'
                }
            }

            stage('License & Vulnerability Scanning') {
                steps {
                    echo 'no License & Vulnerability Scanning defined'
                }
            }
        }
    }

    stage('Publish Library') {
      when {
        branch 'master'
      }
      steps {
        gradlew('publish')
      }
    }
  }
  post {
      success {
          archiveArtifacts artifacts: '**/build/libs/**/*.jar', fingerprint: true
      }
  }
}

def gradlew(String commands) {
    sh "./gradlew --no-daemon --console=plain --refresh-dependencies ${commands}"
}