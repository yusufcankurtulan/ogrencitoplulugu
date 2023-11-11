package com.example.ogrencitoplulugu

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


//import kotlinx.coroutines.flow.internal.NoOpContinuation.context
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun GirisEkrani(navController: NavHostController) {
    var kayitEkraninaGecis by remember { mutableStateOf(false) }
    val context = LocalContext.current
    if (kayitEkraninaGecis) {
        KayitEkrani(navController, context)
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Kullanıcı Adı Girişi
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Kullanıcı Adı") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            // Şifre Girişi
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Şifre") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) }, // Şifre ikonu burada kullanıldı
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            // Giriş ve Kayıt Ol Butonları
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { /* Giriş işlemi */ }) {
                    Text("Giriş Yap")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(onClick = { kayitEkraninaGecis = true }) {
                    Text("Kayıt Ol")
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KayitEkrani(navController: NavHostController, context: Context) {
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

@Preview(showSystemUi = true)
@Composable
fun Ekran() {
    val navController = rememberNavController()

    // Pass the navController and context to the GirisEkrani composable
    GirisEkrani(navController)
}
