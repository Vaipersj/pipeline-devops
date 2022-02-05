def call(){
  pipeline {
      agent any
      environment {
          NEXUS_USER         = credentials('NEXUS_USER')
          NEXUS_PASSWORD     = credentials('NEXUS_PASS')
      }
      parameters {
          choice choices: ['maven', 'gradle'], description: 'Seleccione una herramienta para preceder a compilar', name: 'compileTool'
          text description: 'Enviar los stages separados por coma (;), vacío si necesita todos los stages', name: 'stages'

      }
      stages {
          stage("Pipeline"){
              steps {
                  script{
                      sh "env"
                      env.TAREA = ""
                      if(params.compileTool == 'maven'){

                        maven.call();
                      }else{
                        sh 'echo ' params.stages
                        gradle.call(params.stages)
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