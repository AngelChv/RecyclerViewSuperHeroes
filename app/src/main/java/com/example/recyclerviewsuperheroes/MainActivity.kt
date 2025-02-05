package com.example.recyclerviewsuperheroes

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.recyclerviewsuperheroes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var actinBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        /*ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        initNavController()
        initToolBar()
        initBottomNavigation()
    }

    private fun initNavController() {
        // Obtener navController a través del navHostFragment
        // Con otras formas como estas me daba error
        // binding.fragmentContainerView.findNavController()
        // Forma correcta / La unica que me funciona
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun initBottomNavigation() {
        binding.bottomNav.setupWithNavController(navController)
        // Si lo quires personalizar se puede hacer:
        /*binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.superHeroRecyclerFragment -> {
                    navController.navigate(R.id.superHeroRecyclerFragment)
                    true
                }
                R.id.superHeroFormFragment -> {
                    navController.navigate(R.id.superHeroFormFragment)
                    true
                }
                R.id.profileFragment -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }
                else -> false
            }
        }*/
    }

    private fun initToolBar() {
        setSupportActionBar(binding.toolbar)

        // Configura el ActionBar para trabajar con NavController
        val appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.open()
        }
    }

    // Para manejar el menú lateral
    private fun initDrawerMenu() {
        actinBarDrawerToggle =
            ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                binding.toolbar,
                R.string.open_drawer,
                R.string.close_drawer,
            )

        binding.drawerLayout.addDrawerListener(actinBarDrawerToggle)
        actinBarDrawerToggle.syncState()

        val navView = binding.navigationView
        navView.setupWithNavController(navController)

        // Configurar el listener del menú lateral
        navView.setNavigationItemSelectedListener { menuItem ->
            // Marcar la opción seleccionada
            menuItem.isChecked = true

            // Cerrar el menú después de seleccionar un item
            binding.drawerLayout.closeDrawer(GravityCompat.START)

            // Navegar según el item seleccionado
            when (menuItem.itemId) {
                R.id.superHeroRecyclerFragment -> navController.navigate(R.id.superHeroRecyclerFragment)
                R.id.superHeroFormFragment -> navController.navigate(R.id.superHeroFormFragment)
                R.id.profileFragment -> navController.navigate(R.id.profileFragment)
            }

            true // Indicar que el item fue manejado
        }
    }

    // Drawer
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Verificar si el item es el botón de hamburguesa (home)
        if (item.itemId == android.R.id.home) {
            // Si el toggle maneja la acción, retorna true
            return if (actinBarDrawerToggle.onOptionsItemSelected(item)) {
                true
            } else {
                // Si no, delega la acción al método de la clase padre
                super.onOptionsItemSelected(item)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Super importante para que funcione el botón de ir hacia atrás en el toolbar!!!
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // necesario para añadir el menú
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}