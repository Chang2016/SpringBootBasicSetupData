pipeline {
  agent any
  stages {
    stage('Initialize') {
      steps {
        sh '''
                    echo "PATH = ${PATH}"
                    
                '''
      }
    }

    stage('Build') {
      steps {
        echo 'Building...'
        echo "Running ${env.BUILD_ID} ${env.BUILD_DISPLAY_NAME} on ${env.NODE_NAME} and JOB ${env.JOB_NAME}"
      }
    }

    stage('Test') {
      steps {
        echo 'Testing...'
      }
    }

    stage('Analyze') {
      steps {
        echo 'Analyzing...'
      }
    }

  }
  tools {
    
    jdk 'jdk8'
  }
}
