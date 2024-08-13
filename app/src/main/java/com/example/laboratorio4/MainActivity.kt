package com.example.laboratorio4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
    // Estado para el nombre de la receta
    val recipeName = remember { mutableStateOf(TextFieldValue("")) }

    // Estado para la URL de la imagen
    val recipeImageUrl = remember { mutableStateOf(TextFieldValue("")) }

    // Estado para la lista de elementos
    val itemList = remember { mutableStateListOf<Pair<String, String>>() }

    Column(modifier = modifier.padding(16.dp)) {
        // TextField para el nombre de la receta
        TextField(
            value = recipeName.value,
            onValueChange = { recipeName.value = it },
            label = { Text("Nombre de la receta") },
            modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(8.dp))

        // TextField para la URL de la imagen
        TextField(
            value = recipeImageUrl.value,
            onValueChange = { recipeImageUrl.value = it },
            label = { Text("URL de la imagen") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Botón para agregar el elemento a la lista

        Button(
            onClick = {
                if (recipeName.value.text.isNotBlank() && recipeImageUrl.value.text.isNotBlank()) {
                    itemList.add(Pair(recipeName.value.text, recipeImageUrl.value.text))
                    recipeName.value = TextFieldValue("") // Limpiar campo de texto
                    recipeImageUrl.value = TextFieldValue("") // Limpiar campo de texto
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

        // Mostrar la lista de elementos usando LazyColumn
        LazyColumn {
            items(itemList.size) { index ->
                RecipeItem(
                    name = itemList[index].first,
                    imageUrl = itemList[index].second
                )
            }
        }
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
                .size(70.dp)
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
