package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

	private final Logger log = LoggerFactory.getLogger(UploadFileServiceImpl.class);
	private final static String DIRECTORIO_UPLOAD = "uploads";

	@Override
	public Resource cargar(String nombreFoto) throws MalformedURLException {
		// Se obtiene la ruta de la imagen y a partir de ella se obtiene el recurso
		Path rutaArchivo = getPath(nombreFoto);
		// Log que muestra la ruta del archivo
		log.info(rutaArchivo.toString());

		// Se crea una instancia urlResource convertira URI
		Resource recurso = new UrlResource(rutaArchivo.toUri());

		// SE valida que el recurso exista y se pueda leer, si no existe se lanza una
		// excepción
		if (!recurso.exists() && !recurso.isReadable()) {
			rutaArchivo = Paths.get("src/main/resources/static/images").resolve("no-usuario.png").toAbsolutePath();
			recurso = new UrlResource(rutaArchivo.toUri());
			log.error("Error, no se pudo cargar la imagen: " + nombreFoto);
		}
		return recurso;
	}

	@Override
	public String copiar(MultipartFile archivo) throws IOException {
		// Se obtiene el nombre original + un nombre aleatorio con la clase UUID y
		// elimina espacios vacios con replace
		String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename().replace(" ", "");
		// Se solicita la ruta del archivo (Se crea carpeta uploads)
		// Se obtiene la ruta completa del archivo con resolve(nombre).toAbsolutePath()
		Path rutaArchivo = getPath(nombreArchivo);
		// Log que muestra la ruta del archivo
		log.info(rutaArchivo.toString());

		// Mueve el archivo subido al servidor con la ruta escogida
		Files.copy(archivo.getInputStream(), rutaArchivo);

		return nombreArchivo;
	}

	//Metodo para eliminar archivos
	@Override
	public boolean eliminar(String nombreFoto) {
		if (nombreFoto != null && nombreFoto.length() > 0) {
			// A partir de la ruta absoluta de la foto anterior se transforma la ruta a un
			// archivo tipo File
			Path rutaFotoAnterior = getPath(nombreFoto);
			File archivoFotoAnterior = rutaFotoAnterior.toFile();
			// SE valida que el archivo exista, se pueda leer y se elimina
			if (archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
				archivoFotoAnterior.delete();
				return true;
			}

		}
		return false;
	}

	@Override
	public Path getPath(String nombreFoto) {
		return Paths.get(DIRECTORIO_UPLOAD).resolve(nombreFoto).toAbsolutePath();
	}

}
