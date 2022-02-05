def call(){
  pipeline {
      agent any
      environment {
          NEXUS_USER         = credentials('NEXUS-USER')
          NEXUS_PASSWORD     = credentials('NEXUS-PASS')
      }
      parameters {
          choice choices: ['maven', 'gradle'], description: 'Seleccione una herramienta para preceder a compilar', name: 'compileTool'
      }
      stages {
          stage("Pipeline"){
              steps {
                  script{
                      sh "env"
                      env.TAREA = ""
                      if(params.compileTool == 'maven'){
                          //compilar maven
                          //def executor = load "maven.groovy"
                          //executor.call()
                        maven.call();
                      }else{
                          //compilar gradle
                          //def executor = load "gradle.groovy"
                          //executor.call()
                        gradle.call()
                      }
                  }
              }
              post{
          success{
            slackSend color: 'good', message: "[Mentor] [${JOB_NAME}] [${BUILD_TAG}] Ejecucion Exitosa", teamDomain: 'dipdevopsusac-tr94431'
          }
          failure{
            slackSend color: 'danger', message: "[Mentor] [${env.JOB_NAME}] [${BUILD_TAG}] Ejecucion fallida en stage [${env.TAREA}]", teamDomain: 'dipdevopsusac-tr94431'
          }
        }
          }
      }
  }
}
return this;