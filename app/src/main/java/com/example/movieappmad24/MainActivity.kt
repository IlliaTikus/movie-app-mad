package com.example.movieappmad24

import android.content.res.Resources.Theme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movieappmad24.models.Movie
import com.example.movieappmad24.models.getMovies
import com.example.movieappmad24.ui.theme.MovieAppMAD24Theme
import com.example.movieappmad24.ui.theme.Typography
import kotlinx.coroutines.delay
import java.util.Vector
import coil.compose.rememberImagePainter

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieAppMAD24Theme {
                val items = listOf(
                    BottomNavigationItem(
                        title = "Home",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Outlined.Home
                    ),
                    BottomNavigationItem(
                        title = "Watchlist",
                        selectedIcon = Icons.Filled.Star,
                        unselectedIcon = Icons.Outlined.Star
                    )
                )

                var selectedItemIndex by rememberSaveable {
                    mutableStateOf(0)
                }

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            CenterAlignedTopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    titleContentColor = MaterialTheme.colorScheme.primary,
                                ),
                                title = {
                                    Text(
                                        "Movie App",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            )
                        },
                        bottomBar = {
                            NavigationBar {
                                items.forEachIndexed { index, item ->
                                    NavigationBarItem(
                                        selected = selectedItemIndex == index,
                                        onClick = {
                                            selectedItemIndex = index
                                            // navController.navigate(item.title)
                                        },
                                        label = {
                                            Text(text = item.title)
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if (index == selectedItemIndex) {
                                                    item.selectedIcon
                                                } else item.unselectedIcon,
                                                contentDescription = item.title
                                            )
                                        }
                                    )
                                }
                            }
                        },
                        content = { paddingValues ->
                            MovieList(movies = getMovies(), paddingValues)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MovieList(movies: List<Movie> = getMovies(), paddingValues: PaddingValues) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues)
    ) {
        items(movies) { movie ->
            MovieRow(movie)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieRow(movie: Movie) {
    var showDetails by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = ShapeDefaults.Medium,
        onClick = {
            showDetails = !showDetails
        },
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (movie.images.isNotEmpty()) {
                    MovieImage(imageUrl = movie.images.first())
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Icon(
                        tint = MaterialTheme.colorScheme.secondary,
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Add to favorites"
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = movie.title,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Bold,
                    fontStyle = MaterialTheme.typography.titleLarge.fontStyle
                )
                Icon(
                    modifier = Modifier
                        .clickable {
                            showDetails = !showDetails
                        },
                    imageVector =
                    if (showDetails) Icons.Filled.KeyboardArrowDown
                    else Icons.Default.KeyboardArrowUp, contentDescription = "show more"
                )
            }
            if (showDetails) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                ) {
                    val movieDetails = listOf(
                        "Director: " to movie.director,
                        "Released: " to movie.year,
                        "Genre: " to movie.genre,
                        "Actors: " to movie.actors,
                        "Rating: " to movie.rating
                    )

                    movieDetails.forEach { (label, value) ->
                        MovieDetail(label = label, value = value)
                    }

                    Divider(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        color = Color.Gray.copy(alpha = 0.6f),
                        thickness = 1.dp
                    )

                    MoviePlot(plot = movie.plot)
                }
            }
        }
    }
}

//Refactoring: image from a URL
@Composable
fun MovieImage(imageUrl: String) {
    val imagePainter = rememberImagePainter(data = imageUrl)
    Image(
        painter = imagePainter,
        contentDescription = "Movie image",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth()
    )
}

//Refactoring: details
@Composable
fun MovieDetail(label: String, value: String) {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
        )
        Text(
            text = value,
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
        )
    }
}

//Refactoring: the plot of the movie
@Composable
fun MoviePlot(plot: String) {
    Text(
        modifier = Modifier.padding(10.dp),
        text = "Plot: $plot",
        fontSize = MaterialTheme.typography.titleSmall.fontSize
    )
}


@Preview
@Composable
fun DefaultPreview() {
    MovieAppMAD24Theme {
        MovieList(movies = getMovies(), paddingValues = PaddingValues(0.dp))
    }
}
