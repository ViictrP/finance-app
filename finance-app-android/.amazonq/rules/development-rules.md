# Finance App - Regras de Desenvolvimento para Amazon Q

## 🚨 REGRAS CRÍTICAS - NUNCA QUEBRAR

### **1. Pull-to-Refresh**
- ❌ **NUNCA** remover `PullToRefreshContainer` do `BalanceScreen`
- ✅ Sempre preservar funcionalidades de refresh existentes
- ✅ Verificar se há pull-to-refresh antes de modificar telas

### **2. Navegação e Rotas**
- ✅ Sempre usar `route.split("?").first()` para comparar rotas com parâmetros
- ✅ Manter `backButtonRoutes` atualizado com rotas base (sem parâmetros)
- ✅ Testar navegação após mudanças em rotas

### **3. ViewModels Singleton**
- ✅ Usar `rememberBalanceViewModel()` e `rememberAuthViewModel()`
- ✅ Escopo sempre na `ComponentActivity` para compartilhamento
- ❌ **NUNCA** criar instâncias separadas por tela
- ✅ Centralizar lógica em `ViewModelUtils.kt`

## 📋 PADRÕES DE CÓDIGO

### **Imports e Estrutura**
```kotlin
// ✅ Ordem correta de imports
import androidx.compose.* // Compose primeiro
import androidx.hilt.* // Hilt
import com.viictrp.financeapp.* // Projeto
```

### **ViewModels**
```kotlin
// ✅ Padrão correto
@Composable
fun MyScreen() {
    val viewModel = rememberBalanceViewModel()
    val authViewModel = rememberAuthViewModel()
}

// ❌ Evitar
val context = LocalContext.current
val viewModel = hiltViewModel<BalanceViewModel>(context as ComponentActivity)
```

### **Persistência de Dados**
```kotlin
// ✅ Sempre salvar dados críticos localmente
override suspend fun saveUser(user: UserDTO) {
    userDao.saveUser(userEntity)
}

// ✅ Carregar dados locais primeiro, depois servidor
val localUser = userRepository.getUser()
if (localUser != null) {
    _user.value = localUser
    _isAuthenticated.value = true
}
```

## 🔧 ARQUITETURA

### **Camadas**
1. **UI Layer**: Screens + Components
2. **Domain Layer**: UseCases + Repositories (interfaces)
3. **Data Layer**: RepositoryImpl + Local/Remote sources

### **Dependency Injection**
- ✅ Usar Hilt para todas as dependências
- ✅ Módulos separados: `DatabaseModule`, `RepositoryModule`, `UseCaseModule`
- ✅ Singleton para repositórios e ViewModels

### **Banco de Dados**
- ✅ Sempre criar migrações para mudanças de schema
- ✅ Usar Room para persistência local
- ✅ Fallback para dados locais em caso de erro de rede

## 🎯 BOAS PRÁTICAS ESPECÍFICAS

### **Header Component**
- ✅ Sempre mostrar informações do usuário quando disponível
- ✅ Botão voltar baseado em `backButtonRoutes`
- ✅ Usar dados persistidos para evitar tela em branco

### **Navegação**
- ✅ Usar `financeAppNavController` para navegação customizada
- ✅ Manter estado de navegação consistente
- ✅ Testar deep links e parâmetros de rota

### **Estados de Loading**
- ✅ Sempre mostrar loading states
- ✅ Usar `collectAsState()` para observar ViewModels
- ✅ Implementar estados de erro e retry

### **Testes**
- ✅ Build deve sempre passar: `./gradlew build --no-daemon -x test -x lint`
- ✅ Testar funcionalidades críticas após mudanças
- ✅ Verificar warnings e corrigi-los quando possível

## 🚀 WORKFLOW DE DESENVOLVIMENTO

### **Antes de Modificar**
1. ✅ Ler código existente completamente
2. ✅ Identificar funcionalidades que NÃO devem ser removidas
3. ✅ Verificar dependências e imports

### **Durante Modificação**
1. ✅ Fazer mudanças mínimas necessárias
2. ✅ Preservar funcionalidades existentes
3. ✅ Seguir padrões estabelecidos

### **Após Modificação**
1. ✅ Executar build para verificar erros
2. ✅ Testar funcionalidades afetadas
3. ✅ Documentar mudanças importantes

## 📝 CHECKLIST DE REVISÃO

### **Antes de Finalizar**
- [ ] Build passa sem erros
- [ ] Pull-to-refresh preservado onde existia
- [ ] ViewModels usando funções helper
- [ ] Navegação funcionando corretamente
- [ ] Dados persistindo localmente
- [ ] Estados de loading implementados
- [ ] Imports organizados
- [ ] Código limpo e mínimo

### **Funcionalidades Críticas**
- [ ] Login/Logout funcionando
- [ ] Dados do usuário persistindo
- [ ] Navegação entre telas
- [ ] Pull-to-refresh em listas
- [ ] Estados de loading/erro
- [ ] Botão voltar nas telas corretas

## 🎨 PADRÕES DE UI

### **Composables**
- ✅ Usar `@OptIn` para APIs experimentais quando necessário
- ✅ Parâmetros opcionais com valores padrão
- ✅ Separar lógica de UI da lógica de negócio

### **Estados**
- ✅ Usar `collectAsState()` para observar flows
- ✅ `remember` para valores que devem persistir na recomposição
- ✅ `LaunchedEffect` para efeitos colaterais

## 🔄 VERSIONAMENTO

### **Banco de Dados**
- ✅ Incrementar versão sempre que mudar schema
- ✅ Criar migração correspondente
- ✅ Testar migração com dados existentes

### **Dependências**
- ✅ Manter versões consistentes
- ✅ Atualizar com cuidado
- ✅ Testar após atualizações

---

**Criado em:** 2025-01-25  
**Última atualização:** Fase 1 - Correções de Navegação e Persistência  
**Próxima revisão:** Fase 2
