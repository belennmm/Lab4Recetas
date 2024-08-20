package com.example.laboratorio4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.laboratorio4.ui.theme.Laboratorio4Theme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Laboratorio4Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RecipeApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun RecipeApp(modifier: Modifier = Modifier) {
    // field nombre de la receta (mutable)
    val recipeName = remember { mutableStateOf(TextFieldValue("")) }

    // field URL imagen (mutable)
    val recipeImageUrl = remember { mutableStateOf(TextFieldValue("")) }

    // lista de los elementos que se ingresan
    val itemList = remember { mutableStateListOf<Pair<String, String>>() }

    // Estado del Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.padding(16.dp)) {

        // título
        Text(
            text = "Detalles de la Receta",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // mostrar "nombre de la receta"
        TextField(
            value = recipeName.value,
            onValueChange = { recipeName.value = it },
            label = { Text("Nombre de la receta") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // mostrar "URL de la imagen"
        TextField(
            value = recipeImageUrl.value,
            onValueChange = { recipeImageUrl.value = it },
            label = { Text("URL de la imagen") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // botón que agrega los elementos
        Button(
            onClick = {
                val recipeNameText = recipeName.value.text.trim() // quitar espacios en blanco
                val recipeImageText = recipeImageUrl.value.text.trim()

                // aprobar si no hay información ingresada
                if (recipeNameText.isNotBlank() && recipeImageText.isNotBlank()) {
                    val recipeExists = itemList.any { it.first.equals(recipeNameText, ignoreCase = true) }

                    if (!recipeExists) {
                        // se agrega solo si no existe
                        itemList.add(Pair(recipeNameText, recipeImageText))
                        recipeName.value = TextFieldValue("") // se limpia el nombre
                        recipeImageUrl.value = TextFieldValue("") // se limpia la URL
                    } else {
                        // si ya existe el nombre se muestra:
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("La receta ya existe")
                        }
                    }
                } else {
                    // si no hay nada ingresado se muestra:
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Por favor, ingrese el nombre y la URL de la receta")
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF006C4C),
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        //se muestran los elementos usando LazyColumn
        LazyColumn {
            items(itemList.size) { index ->
                RecipeItem(
                    name = itemList[index].first,
                    imageUrl = itemList[index].second
                )
            }
        }

        // SnackbarHost para mostrar los mensajes
        SnackbarHost(hostState = snackbarHostState)
    }
}

@Composable
fun RecipeItem(name: String, imageUrl: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text(name, style = MaterialTheme.typography.bodyLarge)
        Image(
            painter = rememberImagePainter(data = imageUrl),
            contentDescription = "Imagen de la receta",
            modifier = Modifier
                .size(75.dp)
                .align(androidx.compose.ui.Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeAppPreview() {
    Laboratorio4Theme {
        RecipeApp()
    }
}
