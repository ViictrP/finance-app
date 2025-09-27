# Plano de Refatoração do Projeto

Este documento descreve o plano de ação para refatorar o projeto Android, aplicando princípios de Domain-Driven Design (DDD), Clean Architecture e outras boas práticas de desenvolvimento com Kotlin e Jetpack Compose.

---

### **Fase Pré-0: Refatoração das Rotas**

1.  **Análise:**
    *   Examinar `MainScreen.kt`, `FinanceAppNavController.kt` e qualquer outra parte do código que realize chamadas de navegação.
    *   Mapear todas as ocorrências de strings literais (ex: `"secure/home"`) usadas para definir rotas e navegar.
    *   Identificar onde as constantes de `PublicDestinations` e `SecureDestinations` são usadas.
2.  **Ação:**
    *   Substituir `startDestination = PublicDestinations.SPLASH_ROUTE` por `startDestination = Screen.Splash.route` no `NavHost` principal em `MainScreen.kt`.
    *   Substituir `route = PublicDestinations.SPLASH_ROUTE` por `route = Screen.Splash.route` na definição do composable `SplashScreen` em `MainScreen.kt`.
    *   Substituir `route = PublicDestinations.LOGIN_ROUTE` por `route = Screen.Login.route` na definição do composable `LoginScreen` em `MainScreen.kt`.
    *   Substituir `PublicDestinations.LOGIN_ROUTE` por `Screen.Login.route` na chamada `financeAppNavController.navigateTo` dentro do composable `LoginScreen` em `MainScreen.kt`.
    *   Substituir `route = SecureDestinations.SECURE_ROUTE` por `route = Screen.Secure.route` na definição do composable `SecureContainer` em `MainScreen.kt`.
    *   Substituir as rotas construídas por concatenação de strings (ex: `"${SecureDestinations.TRANSACTION_ROUTE}/..."`) pelas rotas geradas por `Screen.Transaction(0, "").route` e `Screen.Invoice(0, "").route` nas definições dos composables `TransactionScreen` e `InvoiceScreen` em `MainScreen.kt`.
    *   Substituir `startDestination = SecureDestinations.HOME_ROUTE` por `startDestination = Screen.Home.route` no `NavHost` aninhado dentro de `SecureContainer` em `MainScreen.kt`.
    *   Substituir `composable(SecureDestinations.HOME_ROUTE)` por `composable(Screen.Home.route)` na definição do composable `HomeScreen` em `MainScreen.kt`.
    *   Substituir `SecureDestinations.BALANCE_ROUTE` por `Screen.Balance.route` nas chamadas `nestedNavController.navigateTo` dentro do composable `HomeScreen` em `MainScreen.kt`.
    *   Substituir `SecureDestinations.HOME_ROUTE` por `Screen.Home.route` nas chamadas `onNavigation` dentro do composable `HomeScreen` em `MainScreen.kt`.
    *   Substituir `SecureDestinations.CREDIT_CARD_ROUTE` por `Screen.CreditCard.route` na definição do composable `CreditCardScreen` em `MainScreen.kt`.
    *   Substituir `SecureDestinations.BALANCE_ROUTE` por `Screen.Balance.route` na definição do composable `BalanceScreen` em `MainScreen.kt`.
    *   Substituir `SecureDestinations.TRANSACTION_FORM_ROUTE` por `Screen.TransactionForm.route` na definição do composable `TransactionFormScreen` em `MainScreen.kt`.
    *   Substituir `SecureDestinations.CREDIT_CARD_FORM_ROUTE` por `Screen.CreditCardForm.route` na definição do composable `CreditCardFormScreen` em `MainScreen.kt`.
    *   Adicionar `import com.viictrp.financeapp.ui.navigation.Screen` em `MainScreen.kt`.
3.  **Verificação:** Executar `./gradlew build --no-daemon -x test` para garantir que não há erros de compilação.

---

### **Fase 0: Configurar Injeção de Dependência para ViewModels (Hilt)**

1.  **Análise:**
    *   Verificar os arquivos `build.gradle.kts` para confirmar a configuração do Hilt (já confirmado que está configurado).
    *   Analisar `MainScreen.kt` e os composables das telas (`SplashScreen`, `LoginScreen`, `SecureContainer`, `HomeScreen`, `CreditCardScreen`, `BalanceScreen`, `TransactionFormScreen`, `CreditCardFormScreen`, `TransactionScreen`, `InvoiceScreen`) para identificar como os ViewModels são atualmente instanciados e passados como parâmetros.
2.  **Ação:**
    *   Remover os parâmetros `viewModel` ou `authViewModel` das assinaturas das funções `@Composable` das telas.
    *   Dentro de cada composable que precisa de um ViewModel, obter a instância usando `val viewModel: MyViewModel = hiltViewModel()`.
    *   Adicionar `import androidx.hilt.navigation.compose.hiltViewModel` no topo de cada arquivo de tela onde `hiltViewModel()` é usado.
3.  **Verificação:** Executar `./gradlew build --no-daemon -x test` para garantir que não há erros de compilação.

---

### **Fase 1: Estruturar a Camada de Domínio (DDD)**

1.  **Análise:**
    *   Analisar detalhadamente os ViewModels (começando pelo `BalanceViewModel` e `AuthViewModel`) para identificar todas as lógicas de negócio (cálculos, regras de validação, orquestração de chamadas de dados, etc.) que podem ser extraídas para Casos de Uso.
    *   Identificar as entidades de domínio e os contratos de repositório que precisam ser definidos.
2.  **Ação:**
    *   Criar a estrutura de pacotes para a camada `domain` (ex: `com.viictrp.financeapp.domain.usecase`, `com.viictrp.financeapp.domain.repository`, `com.viictrp.financeapp.domain.model`).
    *   Definir as interfaces dos repositórios (ex: `BalanceRepository`, `AuthRepository`) dentro da `domain`.
    *   Criar Casos de Uso (`*UseCase`) para encapsular cada lógica de negócio específica (ex: `GetCurrentBalanceUseCase`, `LoginUserUseCase`), movendo a lógica de dentro dos ViewModels para eles.
    *   Atualizar os ViewModels para injetar e utilizar esses Casos de Uso.
3.  **Verificação:** Executar `./gradlew build --no-daemon -x test` para garantir que não há erros de compilação.

---

### **Fase 2: Estruturar a Camada de Dados (DDD)**

1.  **Análise:**
    *   Mapear as fontes de dados atuais nos pacotes `data/remote` (Apollo/GraphQL) e `data/local` (Room).
    *   Analisar as implementações dos repositórios existentes e como eles interagem com as fontes de dados.
2.  **Ação:**
    *   Mover as implementações concretas dos repositórios (ex: `BalanceRepositoryImpl`, `AuthRepositoryImpl`) para a camada `data` (ex: `com.viictrp.financeapp.data.repository`).
    *   Fazer com que as implementações dos repositórios na camada `data` implementem as interfaces definidas na camada `domain`.
    *   Configurar os módulos Hilt para fornecer as implementações corretas dos repositórios.
3.  **Verificação:** Executar `./gradlew build --no-daemon -x test` para garantir que não há erros de compilação.

---

### **Fase 3: Refatorar a Camada de Apresentação (MVI)**

1.  **Análise:**
    *   Analisar a interação entre as telas e os ViewModels para definir os `States` (o que a UI precisa para se desenhar) e os `Intents` (as ações que o usuário pode realizar) para cada tela.
2.  **Ação:**
    *   Refatorar os ViewModels para seguir o padrão MVI, expondo um `StateFlow` de um único objeto de `State` e recebendo `Intents`.
    *   Conectar os Casos de Uso dentro do ViewModel para processar os `Intents` e gerar novos `States`.
    *   Simplificar as telas para apenas observar o `State` e enviar `Intents` ao ViewModel, sem conter lógica de negócio.
3.  **Verificação:** Executar `./gradlew build --no-daemon -x test` para garantir que não há erros de compilação.
