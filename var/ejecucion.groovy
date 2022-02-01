/*

	forma de invocación de método call:

	def ejecucion = load 'script.groovy'
	ejecucion.call()

*/

def call(){
  
  pipeline {
    agent any
    environment {
        NEXUS_USER         = credentials('NEXUS-USER')
        NEXUS_PASSWORD     = credentials('NEXUS-PASS')
    }
    parameters {
        choice(
            name:'compileTool',
            choices: ['Maven', 'Gradle'],
            description: 'Seleccione herramienta de compilacion'
        )
    }
    stages {
        stage("Pipeline"){
            steps {
                slackSend color: 'blue', iconEmoji: ':alert:', username: 'CBISSOTTO', message: "[Su Nombre] [${JOB_NAME}] [${BUILD_TAG}] Prueba iconEmoji", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'token-slack'
                script{
                    sh "env"
                  switch(params.compileTool)
                    {
                        case 'Maven':
                            //def ejecucion = load 'maven.groovy'
                            maven.call();
                        break;
                        case 'Gradle':
                            //def ejecucion = load 'gradle.groovy'
                            gradle.call()
                        break;
                    }
                }
            }
            post{
                success{
                    slackSend color: 'good', message: "[Su Nombre] [${JOB_NAME}] [${BUILD_TAG}] Ejecucion Exitosa", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'token-slack'
                }
                failure{
                    slackSend color: 'danger', message: "[Su Nombre] [${env.JOB_NAME}] [${BUILD_TAG}] Ejecucion fallida en stage [${env.TAREA}]" , teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'token-slack'
                }
            }
        }
    }
}

}

return this;