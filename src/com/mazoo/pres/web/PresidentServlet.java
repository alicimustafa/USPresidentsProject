package com.mazoo.pres.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mazoo.pres.data.President;
import com.mazoo.pres.data.PresidentDAO;

/**
 * Servlet implementation class PresidentServlet
 */
@SuppressWarnings("serial")
public class PresidentServlet extends HttpServlet {
	private PresidentDAO presDAO;
//	private List<President> curList;
//	private int presIndex = 0;
	
	public void init() throws ServletException {
		presDAO = new PresidentDAO(getServletContext());
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int presIndex = 0;
		HttpSession session = req.getSession();
		List<President> curList = (List<President>)session.getAttribute("presList");
		
        if(curList == null) {
            curList = Collections.synchronizedList(new ArrayList<>());
            session.setAttribute("presList", curList);
        }
//        curList = presDAO.getAllPres();

		String typeSearch = req.getParameter("presSubmit");
		switch (typeSearch) {
		case "all presidents":
			curList = new ArrayList<>(presDAO.getAllPres());
			break;
		case "filter":	
			curList = new ArrayList<>(listFilter(req.getParameter("filterVal")));
			System.out.println(curList.get(0).getName());
			break;
		case "go to":
//			curList = new ArrayList<>(presDAO.getAllPres());
			presIndex = Integer.parseInt(req.getParameter("term")) - 1;
			break;

		}
		session.setAttribute("presList", curList);
		session.setAttribute("next", nextPresIndex(presIndex, curList.size()) + 1);
		session.setAttribute("pre", prevPresIndex(presIndex, curList.size()) + 1);
		session.setAttribute("pres", curList.get(presIndex));
		req.getRequestDispatcher("WEB-INF/president.jsp").forward(req, resp);
	}
	
	private int nextPresIndex(int curIndex, int size) {
		int nextIndex = curIndex;
		if(curIndex == size -1) {				
			nextIndex = 0;
		} else {
			nextIndex++;
		}
		return nextIndex;
	}
	
	private int prevPresIndex(int curIndex, int size) {
		int preIndex = curIndex;
		if(curIndex == 0) {				
			preIndex = size - 1;
		} else {
			preIndex--;
		}
		return preIndex;
	}
	
	
	private List<President> listFilter(String filter){
		List<President> filtered = new ArrayList<>();
		for (President president :presDAO.getAllPres()) {
			if(president.getParty().equals(filter)) {
				filtered.add(president);
			}
		}
		return filtered;
	}
	
}
