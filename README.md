# dendê-eventos-cli 🌴🎟️

Este projeto é o **sistema principal (CLI)** desenvolvido para a **Primeira Atividade (OAT 1)** do curso de Sistemas de Informação no **Centro Universitário de Excelência (UNEX)** — **Unidade Feira de Santana**.

O objetivo do projeto é apoiar os estudantes no desenvolvimento de conceitos fundamentais de **Programação Orientada a Objetos (POO)** e **Lógica de Programação**, aplicados à construção de um sistema de gestão utilizando a linguagem **Kotlin**.

---

## 📚 Contexto Acadêmico

Este projeto foi desenvolvido como parte das atividades práticas do semestre.

A proposta é que os estudantes implementem, testem e validem um sistema completo de ponta a ponta via Console Application, compreendendo o uso de estruturas de dados em memória (Listas), controle de fluxo contínuo (loops e condicionais), encapsulamento e modelagem de domínio (Data Classes e Enums). 

Toda a persistência de dados ocorre estritamente em memória RAM, reforçando o aprendizado na manipulação de coleções nativas do Kotlin sem a abstração de um banco de dados externo.

---

## 🏢 Sobre a Dendê SoftHouse (Empresa Fictícia)

A **Dendê SoftHouse** é uma **empresa fictícia de desenvolvimento de software**, criada exclusivamente para fins acadêmicos nas atividades da universidade.

Ela é responsável pelo desenvolvimento do aplicativo **Dendê Eventos**, uma iniciativa local voltada para:

- Divulgação de eventos culturais, acadêmicos e sociais;
- Gestão de eventos (datas, locais, precificação, lotação);
- Venda e controle de ingressos e participantes.

Este projeto representa o **Módulo Core** (regras de negócio e backend simulado) do sistema Dendê Eventos.

---

## 🎯 Objetivo do Projeto

Implementar um sistema robusto dividido em três grandes pilares lógicos (User Stories), contendo as seguintes funcionalidades:

- **Módulo de Autenticação e Perfis (US 01 a 06):** - Cadastro, login e edição de Usuários Comuns.
  - Cadastro de Organizadores (com suporte para Pessoa Física e validação de Pessoa Jurídica via CNPJ).
  - Cálculo dinâmico de idade, inativação e reativação de contas com bloqueio de segurança.
  
- **Módulo de Gestão de Eventos (US 07 a 10):** - Criação de eventos com validação de datas no futuro e capacidade máxima.
  - Listagem, edição de regras e cancelamento de eventos por parte do organizador responsável.
  
- **Módulo de Experiência do Cliente (US 11 a 14):** - Feed de eventos interativo listando apenas eventos ativos e com vagas disponíveis.
  - Lógica transacional para compra de ingressos (ocupação de vagas) e histórico do usuário.

---

## 🧑‍🤝‍🧑 Identificação da Equipe  

### Nome do Grupo: Equipe PORTO

### Integrantes da Equipe

- Carlos Henrique de Souza Santana Santiago 
- Gustavo Bezerra Nonato 
- Hudnei Sued Passos Santana
- João Guilherme Gonçalves Pinheiro 

---

## 🚀 Como Executar o Projeto

1. Certifique-se de ter o **JDK (Java Development Kit)** e o compilador **Kotlin** instalados na sua máquina.
2. Clone este repositório.
3. Execute o arquivo `Main.kt` a partir da sua IDE (IntelliJ IDEA recomendado) ou via terminal.
4. Utilize o *Seed Data* (dados pré-cadastrados) descritos no código para facilitar os testes de login e fluxos, ou cadastre novos usuários através do menu interativo.
