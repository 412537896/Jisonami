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

public class BlogTypeManagerServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		BlogType blogType = new BlogType();
		String username = req.getSession().getAttribute("username").toString();
		blogType.setBlogAuthor(username);
		blogType.setName(req.getParameter("blogType"));
		BlogTypeService blogTypeService = new BlogTypeService();
		boolean result = false;
		try {
			result = blogTypeService.save(blogType);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(result){
			Map<BlogType,Integer> blogTypeInfo = queryBlogTypeInfo(username);
			req.setAttribute("blogTypeInfo", blogTypeInfo);
			req.getRequestDispatcher("/WEB-INF/content/blog/blogTypeManager.jsp").forward(req, resp);
		}else {
			// 保存博客类型出错
		}
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
