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

## ⚡ Coroutines & Scopes

### ✅ ViewModelScope (Obrigatório)
- **SEMPRE** use `viewModelScope.launch` dentro de ViewModels
- **NUNCA** use `suspend fun` em ViewModels que serão chamadas da UI
- **SEMPRE** deixe o ViewModel gerenciar suas próprias coroutines

```kotlin
// ✅ Correto
fun loadData() {
    viewModelScope.launch {
        try {
            _loading.value = true
            val result = repository.getData()
            _data.value = result
        } catch (e: Exception) {
            _error.value = e.message
        } finally {
            _loading.value = false
        }
    }
}

// ❌ Errado - causa "coroutine left composition"
suspend fun loadData() {
    // Não fazer isso em ViewModels
}
```

### ✅ Scopes na UI
- **NUNCA** use `coroutineScope.launch` para chamar ViewModels
- **SEMPRE** chame funções regulares do ViewModel
- **APENAS** use `rememberCoroutineScope` para ações de UI específicas

```kotlin
// ✅ Correto
@Composable
fun MyScreen() {
    val viewModel = rememberMyViewModel()
    
    Button(onClick = {
        viewModel.loadData() // Função regular
    })
}

// ❌ Errado
@Composable
fun MyScreen() {
    val scope = rememberCoroutineScope()
    
    Button(onClick = {
        scope.launch {
            viewModel.loadData() // Problemático
        }
    })
}
```

### ✅ Tratamento de Erros
- **SEMPRE** use try-catch em coroutines
- **SEMPRE** atualize loading states no finally
- **SEMPRE** trate cancelamento de coroutines

### ✅ Cancelamento
- **SEMPRE** cancele jobs anteriores quando necessário
- **SEMPRE** use `viewModelScope` para cancelamento automático
- **NUNCA** use `GlobalScope` (causa vazamentos)

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
- [ ] ViewModels usam `viewModelScope.launch` (não `suspend fun`)
- [ ] Não há `coroutineScope.launch` chamando ViewModels
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
11. **ViewModelScope**: Use para evitar "coroutine left composition"
12. **Suspend Functions**: Apenas dentro de ViewModels com viewModelScope

---

**Lembre-se**: Essas práticas foram desenvolvidas através da experiência real no projeto. Siga-as para manter qualidade e consistência! 🎯
