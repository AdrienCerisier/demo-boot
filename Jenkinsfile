pipeline {
  agent any
  
  environment {
registry = "adceri/demo-boot"
registryCredential = 'docker-hub'
    }
 
    stages{
    stage('Build Artifact') {
      steps {
        sh "mvn clean package -DskipTests=true"
        archive 'target/*.jar' //so that they can be downloaded later
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
steps{
sh "docker rmi $registry:$BUILD_NUMBER"
}
}
    stage('Deploy to test env') {
steps{
sh"docker stop demo-boot || true"
sh"docker rm demo-boot ||true"
sh "docker run -d -p 8180:8080 --name demo-boot $registry:$BUILD_NUMBER"

}
}
}
}