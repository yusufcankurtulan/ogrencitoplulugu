package com.example.ogrencitoplulugu

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
//import com.example.ogrencitoplulugu.ui.theme.OgrencitopluluguTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

data class University(val name: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController) {
    val universities = listOf("İstanbul Üniversitesi", "Ankara Üniversitesi", "Bilkent Üniversitesi")
    val faculties = mapOf(
        "İstanbul Üniversitesi" to listOf("Fakülte 1", "Fakülte 2", "Fakülte 3"),
        "Ankara Üniversitesi" to listOf("Fakülte A", "Fakülte B", "Fakülte C"),
        "Bilkent Üniversitesi" to listOf("Fakülte X", "Fakülte Y", "Fakülte Z")
    )

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedUniversity by remember { mutableStateOf<String?>(null) }
    var selectedFaculty by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Kayıt Ol", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("Ad") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Soyad") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth(),

            )



// ...

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            var isUniversityExpanded by remember { mutableStateOf(false) }

            // University Dropdown
            OutlinedTextField(
                value = selectedUniversity ?: "",
                onValueChange = { newText ->
                    selectedUniversity = newText
                    selectedFaculty = null
                },
                label = { Text("Üniversite") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                trailingIcon = {
                    IconButton(onClick = { isUniversityExpanded = true }) {
                        Icon(Icons.Filled.ArrowDropDown, "İşaret")
                    }
                }
            )

            DropdownMenu(
                expanded = isUniversityExpanded,
                onDismissRequest = { isUniversityExpanded = false }
            ) {
                universities.forEach { university ->
                    DropdownMenuItem(onClick = {
                        selectedUniversity = university
                        isUniversityExpanded = false
                    }, university)
                }
            }
        }

// Separate Box for Faculty Dropdown
        if (!selectedUniversity.isNullOrBlank()) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                var isFacultyExpanded by remember { mutableStateOf(false) }

                OutlinedTextField(
                    value = selectedFaculty ?: "",
                    onValueChange = { newText -> selectedFaculty = newText },
                    label = { Text("Fakülte") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    trailingIcon = {
                        IconButton(onClick = { isFacultyExpanded = true }) {
                            Icon(Icons.Filled.ArrowDropDown, "İşaret")
                        }
                    }
                )

                DropdownMenu(
                    expanded = isFacultyExpanded,
                    onDismissRequest = { isFacultyExpanded = false }
                ) {
                    faculties[selectedUniversity]?.forEach { faculty ->
                        DropdownMenuItem(onClick = {
                            selectedFaculty = faculty
                            isFacultyExpanded = false
                        }, faculty)
                    }
                }
            }
        }

// ...



        Button(
            onClick = {
                val auth = Firebase.auth

                if (email.endsWith("@std")) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                navController.navigate("welcome")
                            }
                        }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        ) {
            Text("Kayıt Ol")
        }


    }

}









@Composable
fun DropdownMenuItem(onClick: () -> Unit, university: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.CenterStart
    ) {
        Text(university)
    }
}




@Preview(showBackground = true)
@Composable
fun WelcomeScreen() {
    val hayvanIsimleri = listOf("Kedi", "Köpek", "Kuş", "Tavşan", "Kaplumbağa")
    val rastgeleHayvanIsimi = hayvanIsimleri.random()
    Text("Hoşgeldin, $rastgeleHayvanIsimi")
}




@Preview(showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    val navController = rememberNavController()
    RegisterScreen(navController)
}