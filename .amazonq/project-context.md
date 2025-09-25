# Finance App - Contexto do Projeto

## 📱 Sobre o Projeto
- **Nome**: Finance App
- **Plataforma**: Android (Kotlin + Jetpack Compose)
- **Arquitetura**: Clean Architecture + MVVM
- **DI**: Hilt
- **Banco**: Room + SQLite
- **Navegação**: Navigation Compose

## 🏗️ Estrutura do Projeto

### **Módulos Principais**
```
app/src/main/java/com/viictrp/financeapp/
├── auth/                 # Autenticação
├── data/                 # Camada de dados
│   ├── local/           # Room + DAOs
│   ├── remote/          # API + DTOs
│   └── repository/      # Implementações
├── domain/              # Camada de domínio
│   ├── model/          # Modelos de domínio
│   ├── repository/     # Interfaces
│   └── usecase/        # Casos de uso
├── ui/                  # Interface do usuário
│   ├── components/     # Componentes reutilizáveis
│   ├── screens/        # Telas da aplicação
│   ├── theme/          # Tema e cores
│   └── utils/          # Utilitários de UI
├── di/                  # Módulos de injeção
└── modules/             # Módulos específicos
```

### **Telas Principais**
- **SplashScreen**: Verificação de autenticação
- **LoginScreen**: Login com Google
- **HomeScreen**: Dashboard principal
- **BalanceScreen**: Controle de saldo (COM PULL-TO-REFRESH)
- **TransactionScreen**: Detalhes de transações
- **CreditCardScreen**: Gestão de cartões
- **InvoiceScreen**: Faturas de cartão

## 🔧 Tecnologias Utilizadas

### **Core**
- Kotlin
- Jetpack Compose
- Navigation Compose
- Hilt (Dependency Injection)

### **Persistência**
- Room Database
- SharedPreferences (quando necessário)

### **Rede**
- Apollo GraphQL
- Retrofit (se aplicável)

### **Autenticação**
- Google Sign-In
- JWT Tokens

## 🎯 Funcionalidades Implementadas

### **Fase 1 - Concluída**
- ✅ Correção de navegação e botão voltar
- ✅ Persistência de dados de login
- ✅ ViewModels singleton compartilhados
- ✅ Funções helper para ViewModels
- ✅ Migração de banco de dados
- ✅ Estados de loading e erro

### **Próximas Fases**
- 🔄 Fase 2: Novas funcionalidades e melhorias de UX
- 🔄 Fase 3: Otimizações e performance
- 🔄 Fase 4: Testes e qualidade

## 🚨 Pontos de Atenção

### **Funcionalidades Críticas**
1. **Pull-to-Refresh no BalanceScreen** - NUNCA REMOVER
2. **Navegação com parâmetros** - Usar `.split("?").first()`
3. **Persistência de login** - Dados devem sobreviver ao restart
4. **ViewModels singleton** - Usar funções helper

### **Padrões Estabelecidos**
- ViewModels sempre no escopo da Activity
- Dados críticos sempre persistidos localmente
- Estados de loading em todas as operações assíncronas
- Navegação consistente com botão voltar

## 📋 Status Atual

### **Build Status**: ✅ Passing
### **Última Versão DB**: 2 (com tabela user)
### **Cobertura de Telas**: 100% usando ViewModels singleton

### **Próximos Passos**
1. Implementar novas funcionalidades (Fase 2)
2. Melhorar UX/UI
3. Otimizar performance
4. Adicionar testes automatizados

---

**Mantenha este contexto sempre atualizado após mudanças significativas**
