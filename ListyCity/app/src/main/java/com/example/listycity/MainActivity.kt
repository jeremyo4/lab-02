package com.example.listycity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity.ui.theme.ListyCityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cityRepository = CityRepository()
        setContent {
            ListyCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen(
                        cities = cityRepository.cities,
                        onAddCity = { cityRepository.addCity(it) },
                        onDelCity = { cityRepository.delCity(it)},
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

class CityRepository {
    private val _cities = mutableStateListOf(
        "Edmonton", "Vancouver", "Moscow", "Sydney", "Berlin", "Vienna", "Tokyo", "Beijing", "Osaka", "New Delhi"
    )
    val cities: List<String>
        get() = _cities
    fun addCity(city: String) {
        _cities.add(city)
    }

    fun delCity(city: String) {
        _cities.remove(city)
    }
}

@Composable
fun CityListScreen(
    cities: List<String>,
    onAddCity: (String) -> Unit,
    modifier: Modifier = Modifier,
    onDelCity: (String) -> Unit
) {
    var newCityName by remember {mutableStateOf("")}
    var selectedCity by remember { mutableStateOf<String?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = newCityName,
                onValueChange = { newCityName = it },
                label = { Text("City Name") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if(newCityName.isNotBlank()){
                        showAddDialog = true
                    }
                }
            ) {
                Text("Add City")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (selectedCity != null) {
                        showDeleteDialog = true
                    }
                }
            ) {
                Text("Delete City")
            }
        }

        LazyColumn(modifier = modifier.fillMaxSize()) {
            items(cities) { city ->
                CityRow(
                    city = city,
                    selected = city == selectedCity,
                    onSelect = {
                        selectedCity = city
                    }

                )
            }
        }

    }
    if(showDeleteDialog){
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title =  {
                Text("Delete City")
            },
            text = {
                Text("Are you sure you want to delete $selectedCity?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedCity?.let {
                            onDelCity(it)
                        }
                        selectedCity = null
                        showDeleteDialog = false
                    }
                ){
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                    }
                ){
                    Text("Cancel")
                }
            }
        )

    }
    if(showAddDialog) {
        AlertDialog(
            onDismissRequest = {
                showAddDialog = false
            },
            title = {
                Text("Add City")
            },
            text = {
                Text("Are you sure you want to add $newCityName?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAddCity(newCityName)
                        newCityName = ""
                        showAddDialog = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showAddDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}






@Composable
fun CityRow(
    city: String,
    selected: Boolean,
    onSelect: () -> Unit

){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {onSelect()}
            .padding(horizontal = 18.dp, vertical = 14.dp)

    ) {
        Text(
            text = city,
            fontSize = 28.sp,
            modifier = Modifier.weight(1f)
        )

    }
}



