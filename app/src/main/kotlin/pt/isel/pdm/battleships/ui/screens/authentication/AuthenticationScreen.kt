package pt.isel.pdm.battleships.ui.screens.authentication
//
// import androidx.compose.animation.AnimatedVisibility
// import androidx.compose.foundation.layout.Column
// import androidx.compose.foundation.layout.fillMaxSize
// import androidx.compose.material.Text
// import androidx.compose.runtime.Composable
// import androidx.compose.runtime.mutableStateOf
// import androidx.compose.runtime.remember
// import androidx.compose.ui.Alignment
// import androidx.compose.ui.Modifier
// import androidx.compose.ui.res.stringResource
// import androidx.compose.ui.text.input.PasswordVisualTransformation
// import androidx.navigation.NavController
// import pt.isel.pdm.battleships.R
// import pt.isel.pdm.battleships.ui.utils.GoBackButton
// import pt.isel.pdm.battleships.ui.utils.ScreenTitle
//
// /**
// * Authentication page.
// * This screen is responsible for the user login and register,
// * asking for the user name and password.
// *
// * Password is visually protected by [PasswordVisualTransformation].
// * TODO: this protection is fake because the keyboard listener will remember the keys and show them to the user
// *
// * @param navController the navigation controller
// */
// @Composable
// fun AuthenticationScreen(
//    navController: NavController,
//    onLogin: (String, String) -> Unit,
//    onRegister: (String, String, String) -> Unit
// ) {
//    val loginMessage = remember { mutableStateOf<String?>(null) }
//
//    val username = remember { mutableStateOf("") }
//    val password = remember { mutableStateOf("") }
//
//    val loginMessageInvalidUsername = stringResource(id = R.string.login_message_invalid_username)
//    val loginMessageInvalidPassword = stringResource(id = R.string.login_message_invalid_password)
//    val loginMessageUsernameNotFound =
//        stringResource(id = R.string.login_message_username_not_found)
//    val loginMessageWrongPassword = stringResource(id = R.string.login_message_wrong_password)
//    val loginMessageSuccessful = stringResource(id = R.string.login_message_successful)
//
//    val registerMessageUserExists = stringResource(id = R.string.register_message_user_exists)
//    val registerMessageSuccessful = stringResource(id = R.string.register_message_successful)
//
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier.fillMaxSize()
//    ) {
//        ScreenTitle(title = stringResource(R.string.login_title))
//
//        LoginTextFields(
//            username.value,
//            password.value,
//            onUsernameChangeCallback = { username.value = it },
//            onPasswordChangeCallback = { password.value = it }
//        )
//
//        LoginButton(
//            onLoginClickCallback = {
//                validateFields(
//                    username.value,
//                    password.value,
//                    loginMessageInvalidUsername,
//                    loginMessageInvalidPassword
//                )?.let {
//                    loginMessage.value = it
//                    return@LoginButton
//                }
//
//                val hashedPassword = hash(password.value)
//
//                onLogin(username.value, hashedPassword)
//            },
//            onRegisterClickCallback = {
//                validateFields(
//                    username.value,
//                    password.value,
//                    loginMessageInvalidUsername,
//                    loginMessageInvalidPassword
//                )?.let {
//                    loginMessage.value = it
//                    return@LoginButton
//                }
//
//                val hashedPassword = hash(password.value)
//
//                onRegister("", username.value, hashedPassword)
//            }
//        )
//
//        loginMessage.value?.let {
//            AnimatedVisibility(visible = true) {
//                Text(text = it)
//            }
//        }
//
//        GoBackButton(navController)
//    }
// }
