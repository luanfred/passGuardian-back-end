# PassGuardian Backend

## Visão Geral

O backend do **PassGuardian** é responsável por fornecer as APIs e a lógica de negócios necessárias para o funcionamento do aplicativo de gerenciamento de senhas PassGuardian. Ele é desenvolvido em Java utilizando o framework Spring Boot e inclui funcionalidades de criptografia para garantir a segurança dos dados armazenados.

## Funcionalidades

- **Autenticação de Usuário**
- **Armazenamento de Senhas**
- **Criptografia de Senaha**

## Repositórios

- **Backend**: [passGuardian-backend](https://github.com/luanfred/passGuardian-backend)
- **Aplicativo (Frontend)**: [passGuardian-app](https://github.com/luanfred/passGuardian-app)

## Tecnologias Utilizadas

- **Java**: Linguagem de programação utilizada para desenvolver a aplicação.
- **Spring Boot**: Framework para criação de aplicações Java.
- **MySQL**: Banco de dados utilizado para armazenar os dados.
- **Criptografia AES**: Método de criptografia utilizado para proteger as senhas armazenadas.

## Instalação

1. Clone o repositório do backend:
   ```sh
   git clone https://github.com/luanfred/passGuardian-back-end.git
    ```
2. Navegue até o diretório do projeto:
   ```sh
   cd passGuardian-back-end
   ```
3. Execute o projeto utilizando o Maven:
   ```sh
    mvn spring-boot:run
    ```
4. O servidor será iniciado na porta 8080.
    ```
    http://localhost:8080
    ```

## Importante 
- Para executar o projeto é necessário informar as credenciais do banco de dados, chave de criptografia e as credenciais de acesso ao serviço de email.
- Exemplo:
    ```
    spring.datasource.url=jdbc:mysql://localhost:3306/passguardian
    spring.datasource.username=root
    spring.datasource.password=root

    encryption.key=1234567890123456

    spring.mail.host=smtp.gmail.com
    spring.mail.port=587
    spring.mail.username=username@gmail.com
    spring.mail.password=123456
    spring.mail.properties.mail.smtp.auth=true
    spring.mail.properties.mail.smtp.starttls.enable=true
    
    ```



