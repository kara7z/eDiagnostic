<%@ page import="project.enums.Role" %>
<style>
  #navbar{
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    display: flex;
    align-items: center;
    justify-content: space-between;
    background-color: #1a1a1a;
    padding: 12px 24px;
    border-bottom: 1px solid #333;
    z-index: 100;
  }
  #navbar .brand{ font-weight: bold; font-size: 18px; color: white; }
  #navbar .links a{ color: #6cf; margin-left: 20px; }
</style>
<nav id="navbar">
  <span class="brand">eDiagnostic</span>
  <div class="links">
    <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
    <% if (session != null && session.getAttribute("role") == Role.NURSE) { %>
      <a href="${pageContext.request.contextPath}/nurse/reception">Reception</a>
      <a href="${pageContext.request.contextPath}/nurse/daylist">Day list</a>
    <% } %>
    <% if (session != null && session.getAttribute("role") == project.enums.Role.GENERAL_PRACTITIONER) { %>
      <a href="${pageContext.request.contextPath}/general-practitioner/queue">GP Queue</a>
    <% } %>
    <% if (session != null && session.getAttribute("userId") != null) { %>
      <a href="${pageContext.request.contextPath}/logout">Logout</a>
    <% } else { %>
      <a href="${pageContext.request.contextPath}/login">Login</a>
    <% } %>
  </div>
</nav>
