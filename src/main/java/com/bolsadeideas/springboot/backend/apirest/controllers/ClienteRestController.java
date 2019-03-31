package com.bolsadeideas.springboot.backend.apirest.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.models.services.IClienteService;

//Implementa Cors y especifica el dominio. Soporta lista de dominios permitidos(Específico para Angular)
@CrossOrigin(origins = { "http://localhost:4200" })
//Anotación para transformar el controller en controller rest
@RestController
@RequestMapping("/api")
public class ClienteRestController {

	@Autowired
	private IClienteService clienteService;

	@GetMapping("/clientes")
	public List<Cliente> index() {
		return clienteService.findAll();
	}

	//Devuelve cliente convertido en JSON
	@GetMapping("/clientes/{id}")
	public Cliente show(@PathVariable Long id) {
		return clienteService.findById(id);
	}
	
	//Recibe objeto en formato JSON. Se utiliza RequestBody
	@PostMapping("/clientes")
	//Se cambia el código a 201 para determinar que se ha creado contenido en la aplicación. 
	//Si todo sale bien, el status se establece en OK
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente create(@RequestBody Cliente cliente) {
		return clienteService.save(cliente);
	}
	
	//Se utiliza el decorador @PutMapping para actualizar
	@PutMapping("/clientes/{id}")
	//Se cambia el código a 201 para determinar que se ha creado contenido en la aplicación. 
	//Si todo sale bien, el status se establece en OK
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente update(@RequestBody Cliente cliente,@PathVariable Long id) {
		Cliente clienteActual = clienteService.findById(id);
		clienteActual.setApellido(cliente.getApellido());
		clienteActual.setNombre(cliente.getNombre());
		clienteActual.setEmail(cliente.getEmail());
		return clienteService.save(clienteActual);
	}
	
	@DeleteMapping("/clientes/{id}")
	//Como no devuelve ningún contenido se utiliza el parámetro NoContent
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		clienteService.delete(id);
	}
}
