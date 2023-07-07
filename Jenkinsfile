pipeline {
  agent any
  
  environment {
    registry = "adceri/demo-boot"
    registryCredential = 'docker-hub'
    containerName = 'demo-boot' // Nom du container 
    appPort = '8080'
    testPort = '8180'
    prodIP = '13.37.106.112' // IP public de l'instance de production sur aws
    prodPort = '80'
  }
 
  stages {
    stage('Build Artifact') {
      steps {
        sh "mvn clean package -DskipTests=true"
        archive 'target/*.jar' // Pour pouvoir les télécharger ultérieurement
      }
    }
    
    stage('Unit Tests - JUnit and Jacoco') {
      steps {
        sh "mvn test"
      }
      
      post {		
        always {
          junit 'target/surefire-reports/*.xml'
          jacoco execPattern: 'target/jacoco.exec'
        }
      }
    }
    
    stage('Docker Build and Push') {
      steps {
        withDockerRegistry([credentialsId: "docker-hub", url: ""]) {
          sh 'printenv'
          sh 'docker build -t $registry:$BUILD_NUMBER .'
          sh 'docker push $registry:$BUILD_NUMBER'
        }
      }
    }
    
    stage('Remove Unused docker image') {
      steps {
        sh "docker rmi $registry:$BUILD_NUMBER"
      }
    }
    
    stage('Deploy to test env') {
      steps {
        sh "docker stop $containerName || true"
        sh "docker rm $containerName || true"
        sh "docker run -d -p $testPort:$appPort --name $containerName $registry:$BUILD_NUMBER"
      }
    }
    
    stage('Production env') {
      steps {
        input 'Do you approve deployment?'
        echo 'Going into production...'
      }
    }
    
    stage('Deploy to prod env') {
      steps {
        sh "docker -H $prodIP stop $containerName || true"
        sh "docker -H $prodIP rm $containerName || true"
        sh "docker -H $prodIP run -d -p $prodPort:$appPort --name $containerName $registry:$BUILD_NUMBER"
      }
    }
    
    stage('Cleaning test env') {
      steps {
        sh "docker stop $containerName || true"
        sh "docker rm $containerName || true"
        sh "docker rmi \$(docker images -aq) -f"
      }
    }
  }
}