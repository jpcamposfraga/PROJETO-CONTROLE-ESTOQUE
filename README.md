[readme_estoque.md](https://github.com/user-attachments/files/23642245/readme_estoque.md)
# 📦 Sistema de Controle de Estoque

Um sistema completo de gerenciamento de estoque desenvolvido em Java com JavaFX, utilizando SQLite como banco de dados. O sistema oferece controle de produtos, movimentações, usuários e geração de relatórios detalhados.

![Java](https://img.shields.io/badge/Java-17+-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-17-blue)
![SQLite](https://img.shields.io/badge/SQLite-3-green)
![License](https://img.shields.io/badge/License-MIT-yellow)

## 📋 Índice

- [Características](#-características)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação](#-instalação)
- [Configuração](#-configuração)
- [Uso](#-uso)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Funcionalidades](#-funcionalidades)
- [Banco de Dados](#-banco-de-dados)
- [Contribuindo](#-contribuindo)
- [Licença](#-licença)
- [Autores](#-autores)

## ✨ Características

- 🔐 **Sistema de Autenticação**: Login e cadastro de usuários com diferentes níveis de acesso
- 📊 **Dashboard Interativo**: Visão geral do estoque com métricas importantes
- 📦 **Gerenciamento de Produtos**: CRUD completo de produtos
- 🔄 **Controle de Movimentações**: Registro de entradas e saídas de estoque
- 👥 **Gerenciamento de Usuários**: Controle de usuários do sistema (apenas ADMIN)
- 📈 **Relatórios Detalhados**: Geração de relatórios em HTML, CSV e TXT
- ⚠️ **Alertas Inteligentes**: Notificações de estoque baixo e produtos próximos ao vencimento
- 🎨 **Interface Moderna**: UI responsiva com design moderno e intuitivo

## 🛠 Tecnologias Utilizadas

- **Java 17+**: Linguagem principal
- **JavaFX 17**: Framework para interface gráfica
- **SQLite 3.42.0.0**: Banco de dados embarcado
- **JDBC**: Conectividade com banco de dados
- **Maven/Gradle**: Gerenciamento de dependências (opcional)

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

```bash
# Java JDK 17 ou superior
java -version

# JavaFX SDK 17
# Baixar em: https://openjfx.io/

# SQLite JDBC Driver
# sqlite-jdbc-3.42.0.0.jar
```

## 🚀 Instalação

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/sistema-estoque.git
cd sistema-estoque
```

### 2. Baixe as dependências

```bash
# Baixe o JavaFX SDK
wget https://download2.gluonhq.com/openjfx/17/openjfx-17_windows-x64_bin-sdk.zip
unzip openjfx-17_windows-x64_bin-sdk.zip

# Baixe o SQLite JDBC
wget https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.42.0.0/sqlite-jdbc-3.42.0.0.jar
```

### 3. Estrutura de diretórios

```
sistema-estoque/
├── src/
│   └── com/
│       └── estoque/
│           ├── LoginScreen.java
│           ├── CadastroScreen.java
│           ├── MainScreen.java
│           ├── DatabaseManager.java
│           ├── Produtos.java
│           ├── Usuario.java
│           ├── Movimentacao.java
│           ├── Estoque.java
│           ├── EstoqueService.java
│           ├── Relatorio.java
│           ├── ResultadoOperacao.java
│           └── ExcessoesCustomizadas.java
├── lib/
│   └── sqlite-jdbc-3.42.0.0.jar
├── relatorios/
└── README.md
```

## ⚙️ Configuração

### Configuração do JavaFX

Edite o arquivo `launch.json` ou configure seu IDE:

```json
{
    "type": "java",
    "name": "LoginScreen",
    "request": "launch",
    "mainClass": "com.estoque.LoginScreen",
    "vmArgs": "--module-path \"C:/caminho/javafx-sdk-17/lib\" --add-modules javafx.controls",
    "classpath": [
        "${workspaceFolder}/lib/sqlite-jdbc-3.42.0.0.jar"
    ]
}
```

### Compilação via Linha de Comando

```bash
# Compilar
javac --module-path /caminho/javafx-sdk-17/lib \
      --add-modules javafx.controls \
      -cp lib/sqlite-jdbc-3.42.0.0.jar \
      -d bin src/com/estoque/*.java

# Executar
java --module-path /caminho/javafx-sdk-17/lib \
     --add-modules javafx.controls \
     -cp bin:lib/sqlite-jdbc-3.42.0.0.jar \
     com.estoque.LoginScreen
```

## 📖 Uso

### Primeiro Acesso

1. Execute a aplicação
2. Use as credenciais padrão:
   - **Usuário**: `admin`
   - **Senha**: `admin123`
3. Ou crie uma nova conta na tela de login

### Gerenciamento de Produtos

```java
// Adicionar produto
1. Navegue até "Produtos"
2. Clique em "+ Novo Produto"
3. Preencha os campos:
   - Nome
   - Categoria
   - Fornecedor
   - Descrição
   - Preço
   - Quantidade inicial
   - Data de validade
4. Clique em "Salvar"
```

### Registrar Movimentações

```java
// Entrada de estoque
1. Navegue até "Movimentações"
2. Clique em "+ Entrada"
3. Selecione o produto
4. Informe a quantidade
5. Confirme

// Saída de estoque
1. Navegue até "Movimentações"
2. Clique em "- Saída"
3. Selecione o produto
4. Informe a quantidade
5. Confirme (sistema valida estoque disponível)
```

### Gerar Relatórios

```java
1. Navegue até "Relatórios"
2. Escolha o tipo de relatório:
   - Relatório Completo (HTML)
   - Relatório de Produtos
   - Relatório de Movimentações
   - Produtos com Estoque Baixo
3. O arquivo será salvo em /relatorios/
```

## 📁 Estrutura do Projeto

### Classes Principais

#### `LoginScreen.java`
Tela de autenticação do sistema.
```java
- Validação de credenciais
- Redirecionamento para tela principal
- Link para cadastro de novos usuários
```

#### `MainScreen.java`
Tela principal com dashboard e todas as funcionalidades.
```java
- Dashboard com métricas
- Gerenciamento de produtos
- Controle de movimentações
- Geração de relatórios
- Gerenciamento de usuários (ADMIN)
```

#### `DatabaseManager.java`
Singleton responsável por todas as operações de banco de dados.
```java
- Conexão com SQLite
- CRUD de produtos, usuários e movimentações
- Autenticação
- Consultas customizadas
```

#### `EstoqueService.java`
Camada de serviço com lógica de negócio.
```java
- Validações de entrada
- Operações de estoque
- Cálculos e análises
- Controle de regras de negócio
```

#### `Relatorio.java`
Geração de relatórios em múltiplos formatos.
```java
- Relatórios HTML, CSV, TXT
- Análises estatísticas
- Produtos mais/menos vendidos
- Alertas de estoque
```

### Modelos de Dados

#### `Produtos.java`
```java
class Produtos {
    - id: int
    - nome: String
    - categoria: String
    - fornecedor: String
    - descricao: String
    - preco: double
    - quantidade: int
    - unidadeMedida: String
    - dataValidade: LocalDate
    - dataCadastro: LocalDateTime
}
```

#### `Usuario.java`
```java
class Usuario {
    - id: int
    - nome: String
    - usuario: String
    - senha: String
    - tipo: String (ADMIN/USER)
}
```

#### `Movimentacao.java`
```java
class Movimentacao {
    - idProduto: int
    - produto: Produtos
    - tipo: tipoMovimentacao (ENTRADA/SAIDA)
    - quantidade: int
    - dataMovimentacao: LocalDateTime
}
```

## 🎯 Funcionalidades

### Dashboard

- **Visão Geral**:
  - Total de produtos cadastrados
  - Produtos com estoque baixo (< 10 unidades)
  - Produtos próximos ao vencimento (< 15 dias)
  - Valor total do estoque

- **Alertas Visuais**:
  - Tabela de produtos com estoque crítico
  - Tabela de produtos próximos ao vencimento

### Gerenciamento de Produtos

- ✅ Cadastrar novos produtos
- ✅ Editar informações de produtos
- ✅ Remover produtos (com validação)
- ✅ Buscar produtos por nome
- ✅ Listar todos os produtos
- ✅ Visualizar detalhes completos

### Controle de Movimentações

- ✅ Registrar entradas de estoque
- ✅ Registrar saídas de estoque
- ✅ Validação de estoque disponível
- ✅ Histórico completo de movimentações
- ✅ Rastreabilidade por usuário

### Gerenciamento de Usuários (ADMIN)

- ✅ Cadastrar novos usuários
- ✅ Editar informações de usuários
- ✅ Remover usuários
- ✅ Definir níveis de acesso (ADMIN/USER)
- ✅ Listar todos os usuários

### Relatórios

#### Formatos Disponíveis
- **HTML**: Relatório visual e interativo
- **CSV**: Para importação em planilhas
- **TXT**: Formato texto simples

#### Tipos de Relatórios
1. **Relatório Completo**:
   - Resumo do estoque
   - Lista de todos os produtos
   - Histórico de movimentações
   - Produtos com estoque baixo
   - Produtos próximos ao vencimento

2. **Relatório de Produtos**:
   - Listagem detalhada de produtos
   - Valores e quantidades
   - Informações de validade

3. **Relatório de Movimentações**:
   - Histórico completo
   - Entradas e saídas
   - Datas e quantidades

4. **Relatório de Estoque Baixo**:
   - Produtos abaixo do limite
   - Alertas por categoria

## 🗄️ Banco de Dados

### Localização
O banco de dados SQLite é criado automaticamente em:
```
Windows: C:\Users\[Usuario]\.estoqueapp\estoque.db
Linux/Mac: /home/[usuario]/.estoqueapp/estoque.db
```

### Estrutura das Tabelas

#### Tabela `usuarios`
```sql
CREATE TABLE usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    usuario TEXT UNIQUE NOT NULL,
    senha TEXT NOT NULL,
    tipo TEXT NOT NULL,
    ativo INTEGER DEFAULT 1,
    data_cadastro TEXT NOT NULL
);
```

#### Tabela `produtos`
```sql
CREATE TABLE produtos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    categoria TEXT NOT NULL,
    fornecedor TEXT,
    descricao TEXT,
    preco REAL NOT NULL,
    quantidade INTEGER DEFAULT 0,
    unidade_medida TEXT,
    data_validade TEXT,
    data_cadastro TEXT NOT NULL
);
```

#### Tabela `movimentacoes`
```sql
CREATE TABLE movimentacoes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    produto_id INTEGER NOT NULL,
    tipo TEXT NOT NULL,
    quantidade INTEGER NOT NULL,
    usuario_id INTEGER,
    data_movimentacao TEXT NOT NULL,
    FOREIGN KEY(produto_id) REFERENCES produtos(id),
    FOREIGN KEY(usuario_id) REFERENCES usuarios(id)
);
```

### Usuário Padrão
```
Usuário: admin
Senha: admin123
Tipo: ADMIN
```

## 🔒 Segurança

- Senhas armazenadas em texto plano (⚠️ **Recomendação**: implementar hashing)
- Validação de níveis de acesso
- Controle de permissões por funcionalidade
- Validação de dados de entrada

## 🚧 Melhorias Futuras

- [ ] Implementar hashing de senhas (BCrypt)
- [ ] Adicionar autenticação JWT
- [ ] Criar backup automático do banco
- [ ] Implementar logs de auditoria
- [ ] Adicionar gráficos e dashboards avançados
- [ ] Suporte a múltiplos idiomas
- [ ] Exportação de relatórios em PDF
- [ ] Notificações por email
- [ ] API REST para integração
- [ ] Aplicativo mobile

## 🤝 Contribuindo

Contribuições são bem-vindas! Para contribuir:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/NovaFuncionalidade`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/NovaFuncionalidade`)
5. Abra um Pull Request

### Padrões de Código

- Use nomes descritivos para variáveis e métodos
- Comente código complexo
- Siga as convenções Java (camelCase, etc.)
- Mantenha métodos pequenos e focados
- Escreva testes quando possível

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👥 Autores

- **João Pedro Penna de Campos Fraga** - *Desenvolvimento Inicial* - [MeuGitHub](https://github.com/jpcamposfraga)


## 🙏 Agradecimentos

- JavaFX pela excelente framework de UI
- SQLite pela simplicidade e eficiência
- Comunidade Java pelo suporte

---

⭐ Se este projeto foi interessante ou útil para você, considere dar uma estrela no GitHub!
