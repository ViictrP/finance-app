# Android Best Practices - Finance App

## 📱 Arquitetura & ViewModels

### ✅ ViewModels Singleton
- **SEMPRE** use `rememberBalanceViewModel()` e `rememberAuthViewModel()`
- **NUNCA** passe ViewModels como parâmetros entre telas
- **SEMPRE** use escopo da Activity para compartilhar estado

```kotlin
// ✅ Correto
val viewModel = rememberBalanceViewModel()

// ❌ Errado
val viewModel = hiltViewModel<BalanceViewModel>()
```

### ✅ Persistência de Dados
- **SEMPRE** persista dados críticos no banco local (Room)
- **SEMPRE** implemente fallback offline
- **SEMPRE** carregue dados locais primeiro, depois sincronize

```kotlin
// ✅ Padrão implementado no AuthViewModel
// 1. Carrega dados locais (resposta rápida)
// 2. Verifica servidor (sincronização)
// 3. Atualiza dados se necessário
```

## 🎨 UI & Compose

### ✅ Navigation & Back Button
- **SEMPRE** use `route.split("?").first()` para comparar rotas com parâmetros
- **NUNCA** compare rotas completas com parâmetros diretamente

```kotlin
// ✅ Correto
val baseRoute = route.split("?").first()
baseRoute in backButtonRoutes

// ❌ Errado
route in backButtonRoutes // Falha com parâmetros
```

### ✅ Pull-to-Refresh
- **NUNCA** remova `PullToRefreshContainer` do BalanceScreen
- **SEMPRE** mantenha funcionalidades de refresh existentes
- **SEMPRE** implemente loading states apropriados

### ✅ Composables Reutilizáveis
- **SEMPRE** crie funções helper para lógica repetitiva
- **SEMPRE** encapsule ViewModels em funções `remember*`
- **SEMPRE** mantenha Composables pequenos e focados

## 🗄️ Dados & Repository

### ✅ Repository Pattern
- **SEMPRE** implemente interface + implementação
- **SEMPRE** injete via Hilt/Dagger
- **SEMPRE** separe dados locais e remotos

### ✅ Database Migrations
- **SEMPRE** incremente versão do banco ao adicionar entidades
- **SEMPRE** implemente migrations para mudanças de schema
- **SEMPRE** teste migrations em diferentes versões

```kotlin
// ✅ Exemplo de migration
private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `user` ...")
    }
}
```

## 🔧 Dependency Injection

### ✅ Hilt/Dagger
- **SEMPRE** use `@Singleton` para repositórios
- **SEMPRE** organize módulos por responsabilidade
- **SEMPRE** injete dependências via construtor

### ✅ Módulos Organizados
```
- DatabaseModule: Room, DAOs
- RepositoryModule: Repository bindings
- NetworkModule: API, HTTP clients
- UseCaseModule: Business logic
```

## 🎯 Performance

### ✅ State Management
- **SEMPRE** use `StateFlow` para estados reativos
- **SEMPRE** colete states com `collectAsState()`
- **NUNCA** faça operações pesadas na UI thread

### ✅ Memory Management
- **SEMPRE** implemente `DisposableEffect` quando necessário
- **SEMPRE** limpe recursos em `onDispose`
- **SEMPRE** use `viewModelScope` para coroutines

## 🔒 Segurança

### ✅ Dados Sensíveis
- **NUNCA** persista tokens de acesso no banco
- **SEMPRE** use dados locais apenas para cache
- **SEMPRE** limpe dados sensíveis no logout

### ✅ Validação
- **SEMPRE** valide dados de entrada
- **SEMPRE** trate erros de rede graciosamente
- **SEMPRE** implemente timeouts apropriados

## 📝 Código Limpo

### ✅ Verificação Antes de Modificações
- **SEMPRE** releia o arquivo antes de fazer alterações
- **NUNCA** sobreescreva código se o contexto não for encontrado
- **SEMPRE** verifique se o código ainda existe no local esperado
- **SEMPRE** confirme que não houve alterações externas

```bash
# ✅ Fluxo correto:
# 1. Ler arquivo atual
# 2. Verificar se código existe
# 3. Se não encontrado, parar e investigar
# 4. Só então fazer modificação

# ❌ Erro comum:
# Tentar modificar código que não existe mais
# Sobreescrever alterações feitas externamente
```

### ✅ Nomenclatura
- **SEMPRE** use nomes descritivos
- **SEMPRE** siga convenções Kotlin
- **SEMPRE** mantenha consistência no projeto

### ✅ Estrutura de Arquivos
```
ui/
├── components/     # Componentes reutilizáveis
├── screens/        # Telas organizadas por feature
├── utils/          # Funções helper
└── theme/          # Temas e estilos

data/
├── local/          # Room, DAOs, Entities
├── remote/         # API, DTOs
└── repository/     # Implementações

domain/
├── model/          # Modelos de domínio
├── repository/     # Interfaces
└── usecase/        # Casos de uso
```

### ✅ Imports
- **SEMPRE** organize imports por categoria
- **SEMPRE** remova imports não utilizados
- **SEMPRE** use imports específicos

## 🧪 Testing

### ✅ Testes Unitários
- **SEMPRE** teste ViewModels
- **SEMPRE** teste Repositories
- **SEMPRE** mocke dependências externas

### ✅ Testes de Integração
- **SEMPRE** teste fluxos completos
- **SEMPRE** teste migrations de banco
- **SEMPRE** teste casos de erro

## 🚀 Build & Deploy

### ✅ Gradle
- **SEMPRE** use versões estáveis
- **SEMPRE** configure ProGuard/R8 adequadamente
- **SEMPRE** otimize builds para CI/CD

### ✅ Versionamento
- **SEMPRE** incremente versionCode
- **SEMPRE** use semantic versioning
- **ALWAYS** documente breaking changes

## 📋 Checklist de PR

Antes de fazer merge, verifique:

- [ ] Código foi verificado antes de modificações
- [ ] Nenhuma alteração externa foi sobrescrita
- [ ] ViewModels usam funções helper (`remember*`)
- [ ] Não há ViewModels passados como parâmetros
- [ ] Pull-to-refresh mantido onde existia
- [ ] Dados críticos persistidos localmente
- [ ] Migrations implementadas se necessário
- [ ] Imports organizados e limpos
- [ ] Testes passando
- [ ] Build sem warnings críticos
- [ ] Funcionalidades existentes preservadas

## 🎯 Lições Aprendidas

1. **Verificação de Código**: Sempre reler arquivos antes de modificar
2. **Singleton ViewModels**: Evita inconsistências de estado
3. **Persistência Local**: Melhora UX e funciona offline
4. **Comparação de Rotas**: Cuidado com parâmetros de query
5. **Funções Helper**: Reduz código repetitivo
6. **Preservar Funcionalidades**: Nunca remover sem avisar
7. **Migrations**: Essenciais para atualizações de banco
8. **Fallback Offline**: Sempre ter plano B
9. **Loading States**: Feedback visual é crucial
10. **Não Sobreescrever**: Respeitar alterações externas

---

**Lembre-se**: Essas práticas foram desenvolvidas através da experiência real no projeto. Siga-as para manter qualidade e consistência! 🎯
