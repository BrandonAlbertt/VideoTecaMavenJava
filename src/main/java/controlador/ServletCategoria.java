package controlador;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import dao.MySqlCategoriaDAO;
import entidad.Categoria;
import entidad.CategoriaInfo;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.ArrayList;


@WebServlet("/ServletCategoria")
public class ServletCategoria extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public ServletCategoria() {
        super();
    }

	
        @Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
            String tipo = request.getParameter("accion");

            if(tipo.equals("lista"))
                listarCategoria(request, response);
            if(tipo.equals("grafico"))
                graficoInfo(request, response);

	}

        //esto lista todas las categorias que tengan
	protected void listarCategoria(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
	List<Categoria> lista = null;
        try {
            lista = new MySqlCategoriaDAO().findAllCategoria();
            System.out.println("Datos de usuarios obtenidos correctamente de la base de datos:");
            if (lista != null) {
                for (Categoria categoria : lista) {
                    System.out.println("ID categoria: " +categoria.getIdCategoria());
                    System.out.println("categoria" +categoria.getNombreCategoria());
                    System.out.println("---------------------------");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al obtener los datos de usuarios de la base de datos: " + e.getMessage());
        }
        request.setAttribute("listatipoCategoria", lista);
        request.getRequestDispatcher("/categoria.jsp").forward(request, response);
	}
        
        
    //este es un metodo que optiene informacion de categoria para mostrar en grafico
    protected void graficoInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<CategoriaInfo> lista = new MySqlCategoriaDAO().findAllCategoriaInfo();

        List<String> nombresCategorias = new ArrayList<>();
        List<Integer> cantidadVideos = new ArrayList<>();

        for (CategoriaInfo categoriaInfo : lista) {
            nombresCategorias.add(categoriaInfo.getNombreCategoria());
            cantidadVideos.add(categoriaInfo.getCantidadVideo());
        }

        // Crear un objeto JSON para los datos
        JsonObject data = new JsonObject();
        data.add("nombreCategorias", new Gson().toJsonTree(nombresCategorias));
        data.add("cantidadVideos", new Gson().toJsonTree(cantidadVideos));

        // Configurar la respuesta
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(data.toString());
    }

        
        
}
