pipeline {
  agent any
  stages {
    stage('Initialize') {
      steps {
        sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
      }
    }

    stage('Build') {
      steps {
        echo 'Building...'
        echo "Running ${env.BUILD_ID} ${env.BUILD_DISPLAY_NAME} on ${env.NODE_NAME} and JOB ${env.JOB_NAME}"
        sh 'mvn clean -DskipITs install'
      }
    }

    stage('Report') {
      steps {
        echo 'Report...'
        junit 'target/surefire-reports/**/*.xml'
        archiveArtifacts 'target/*.jar,target/*.hpi'
      }
    }

    stage('Analyze') {
      environment {
        SCANNERHOME = tool 'SonarQube'
        ORGANIZATION = 'default-organization'
        PROJECT_NAME = 'SpringBootBasicSetupData'
      }
      steps {
        echo 'Analyzing...'
        withSonarQubeEnv(installationName: 'SonarQube', credentialsId: 'jenkinsId') {
          sh '${SCANNERHOME}/bin/sonar-scanner                 -Dsonar.organization=$ORGANIZATION                 -Dsonar.java.binaries=target/classes/                 -Dsonar.projectKey=$PROJECT_NAME                 -Dsonar.sources=./src/main/java/'
        }

        timeout(time: 60, unit: 'SECONDS') {
          waitForQualityGate abortPipeline: true
        }

      }
    }

  }
  tools {
    maven 'Maven 3.3.9'
    jdk 'jdk8'
  }
}
