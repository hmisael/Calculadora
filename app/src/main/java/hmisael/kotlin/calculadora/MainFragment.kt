package hmisael.kotlin.calculadora

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import hmisael.kotlin.calculadora.databinding.FragmentMainBinding


class MainFragment : Fragment(R.layout.fragment_main) {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    //Variables booleanas para validar una operación o el uso del punto decimal
    private var validarOperacion = false
    private var validarDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Botón para limpiar ambos TextView
        binding.btnLimpiar.setOnClickListener {
            limpiarOperaciones()
        }

        //Borra el último dígito ingresado
        binding.btnBorrar.setOnClickListener {
            borrarOperador()
        }

        //Click listener para los botones de cada operación matemática
        binding.btnDividir.setOnClickListener{
            realizarOperacion(it)
        }
        binding.btnMultiplicar.setOnClickListener{
            realizarOperacion(it)
        }
        binding.btnRestar.setOnClickListener{
            realizarOperacion(it)
        }
        binding.btnSumar.setOnClickListener{
            realizarOperacion(it)
        }

        //Click listener de botón igual
        binding.btnIgual.setOnClickListener {
            botonIgual()
        }

        //Click listener de los botones numerados
        binding.btnCero.setOnClickListener {
            agregarNumero(it)
        }
        binding.btnUno.setOnClickListener {
            agregarNumero(it)
        }
        binding.btnDos.setOnClickListener {
            agregarNumero(it)
        }
        binding.btnTres.setOnClickListener {
            agregarNumero(it)
        }
        binding.btnCuatro.setOnClickListener {
            agregarNumero(it)
        }
        binding.btnCinco.setOnClickListener {
            agregarNumero(it)
        }
        binding.btnSeis.setOnClickListener {
            agregarNumero(it)
        }
        binding.btnSiete.setOnClickListener{
            agregarNumero(it)
        }
        binding.btnOcho.setOnClickListener{
            agregarNumero(it)
        }
        binding.btnNueve.setOnClickListener{
            agregarNumero(it)
        }

        //Click listener para el botón decimal (punto)
        binding.btnDecimal.setOnClickListener {
            agregarNumero(it)
        }
    }
    

    //Función para usar los operadores matemáticos
    private fun realizarOperacion(view: View){
        //Si es válido realizar la operación
        if(validarOperacion){
            //Se concatena el signo en el TetView de la operación
            binding.tvOperacion.append((view as Button).text)
            //Se deshabilita la variable para evitar agregar otro signo
            validarOperacion = false
            //Se habilita de nuevo el decimal para ser usado en el siguiente término
            validarDecimal = true
        }
    }

    //Función para operar números o el punto decimal (".")
    private fun agregarNumero(view: View){
        //Si el texto del botón es un "." se concatena a la operación
        if((view as Button).text == "."){
            if(validarDecimal)
                binding.tvOperacion.append(view.text)
            //Se deshabilita variable para evitar agregar más de un decimal
            validarDecimal = false
        }
        //En caso de que el botón presionado sea un número, se concatena a la operación
        else
            binding.tvOperacion.append(view.text)
        validarOperacion = true
        //}
    }

    //Función para el botón igual
    private fun botonIgual(){
        //El contenido del TextView es el resultado de la función
        binding.tvResultado.text = calcularResultado()
    }

    //Función que elimina el contenido de ambos TextView
    private fun limpiarOperaciones(){
        binding.tvOperacion.text = ""
        binding.tvResultado.text = ""
    }

    //Función para borrar caracter del TextView de la operación
    private fun borrarOperador(){
        //Obtener longitud de cadena actual
        val longitud = binding.tvOperacion.length()
        //Si existe un caracter o más, se elimina el último (usando subsecuencia)
        if(longitud > 0)
            binding.tvOperacion.text = binding.tvOperacion.text.subSequence(0, longitud - 1)
    }

    //Función que calcula resultado de la operación matemática
    private fun calcularResultado(): String{

        val operacionMatematica = operacionMatematica()
        //Si se presionó el botón igual con TextView de operación vacío, no se hace nada
        if(operacionMatematica.isEmpty())
            return ("")

        //Si existe contenido en TextView de operación, se procede a calcular
        //Chequear si es una multiplicación o división
        val operacion = verificarMultiDiv(operacionMatematica)
        if(operacion.isEmpty())
            return ("")

        //Realizar suma o resta
        val resultado = calcularSumaResta(operacion)
        return resultado.toString()
    }

    private fun calcularSumaResta(lista: MutableList<Any>): Float{
        var resultado = lista[0] as Float

        for(i in lista.indices){
            if(lista[i] is Char && i != lista.lastIndex){
                val operador = lista[i]
                val siguienteDigito = lista[i + 1] as Float
                if (operador == '+')
                    resultado += siguienteDigito
                if (operador == '-')
                    resultado -= siguienteDigito
            }
        }

        return resultado
    }


    private fun verificarMultiDiv(lista: MutableList<Any>): MutableList<Any>{
        var listaAux = lista
        //Mientras la lista tenga operador de multiplicación o división
        while (listaAux.contains('x') || listaAux.contains('/')){
            listaAux = calcularMultiDiv(listaAux)
        }
        return listaAux
    }

    private fun calcularMultiDiv(lista: MutableList<Any>): MutableList<Any>{
        val listaAux = mutableListOf<Any>()
        //El index es la longitud de la lista
        var reiniciarIndice = lista.size

        //Se recorre los índice i la MutableList (un índice como subconjunto de caracteres)
        for(i in lista.indices){
            //Si el indice encontrado es de tipo Char,
            //Si i no es el último indice de la lista
            //Si i es menor al tamaño de la lista
            if(lista[i] is Char && i != lista.lastIndex && i < reiniciarIndice){
                //Guardar como operador el indice encontrado
                val operador = lista[i]
                //Así, el anterior y siguiente indice son digitos (términos de la operación)
                //La calculadora solo permite operar con dos términos
                val terminoPrevio = lista[i - 1] as Float
                val terminoSiguiente = lista[i + 1] as Float
                //Según el operador, se realiza multiplicación o división
                when(operador){
                    'x' -> {
                        listaAux.add(terminoPrevio * terminoSiguiente)
                        reiniciarIndice = i + 1
                    }
                    '/' -> {
                        listaAux.add(terminoPrevio / terminoSiguiente)
                        reiniciarIndice = i + 1
                    }
                    //Si se ingresó otro tipo de operador
                    else -> {
                        listaAux.add(terminoPrevio)
                        listaAux.add(operador)
                    }
                }
            }
            if(i > reiniciarIndice)
                listaAux.add(lista[i])
        }
        return listaAux
    }

    //Función para tomar los caracteres del String del TextView de la operación
    //y usarlos para operar matemáticamente según su funcionalidad
    private fun operacionMatematica(): MutableList<Any>{
        val listaOperacion = mutableListOf<Any>()
        var termino = ""
        //Para cada caracter del TextView de la operación
        for(caracter in binding.tvOperacion.text){
            //Si el caracter encontrado es un dígito entero o un punto decimal
            if(caracter.isDigit() || caracter == '.')
            //Se agrega el caracter al término matemático
                termino += caracter
            //En caso de ser un operador matemático o un signo igual
            else{
                //el término se agrega a la lista operación como un número Float
                listaOperacion.add(termino.toFloat())
                //se limpia la variable de término para leer el siguiente
                termino = ""
                //Se agrega el operador matemático a la lista de la operación
                listaOperacion.add(caracter)
            }
        }
        //Al terminar de recorrer el texto del TextView,
        // agrego el último término a la lista operación como un número Float
        if(termino != "")
            listaOperacion.add(termino.toFloat())
        //Devuelvo la lista
        return listaOperacion
    }


}

