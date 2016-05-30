package org.jisonami.blog.blogtype;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jisonami.entity.BlogType;
import org.jisonami.service.BlogService;
import org.jisonami.service.BlogTypeService;

public class BlogTypeManagerForwardServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String username = req.getSession().getAttribute("username").toString();
		Map<BlogType,Integer> blogTypeInfo = queryBlogTypeInfo(username);
		req.setAttribute("blogTypeInfo", blogTypeInfo);
		req.getRequestDispatcher("/WEB-INF/content/blog/blogTypeManager.jsp").forward(req, resp);
	}
	
	private Map<BlogType, Integer> queryBlogTypeInfo(String author){
		Map<BlogType,Integer> blogTypeInfo = new HashMap<BlogType,Integer>();
		BlogTypeService blogTypeService = new BlogTypeService();
		List<BlogType> blogTypes = null;
		try {
			blogTypes = blogTypeService.queryByAuthor(author);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		BlogService blogService = new BlogService();
		for (BlogType bt : blogTypes) {
			try {
				int blogCount = blogService.blogCountByBlogType(bt.getId());
				blogTypeInfo.put(bt, blogCount);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return blogTypeInfo;
	}

}
