<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Toy Project</title>
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=914f58637648f96650335fe71e4bc07d"></script>
<%@ include file= "/WEB-INF/views/inc/asset.jsp" %>
</head>
<style>
#map{

	width:90%;
	height:400px;
}

</style>
<body>
	
  <main>
  		<%@include file="/WEB-INF/views/inc/header.jsp" %>
		<section>
			<h2>카카오맵</h2>
			
			<div id="map"></div>
			
			
		</section>
	</main>	
	
	<script>
	var container = document.getElementById('map');
	
	var options = {
		center: new kakao.maps.LatLng(37.499321, 127.033220), //center로 사용될 경도와 위도
		level: 3
	};

	var map = new kakao.maps.Map(container, options);
	
	//map.setCenter(좌표);
	//map.panTo(좌표);
	
	let m = null;
	
	kakao.maps.event.addListener(map, 'click', function(event) {
		
		//event.latLng > new kakao.maps.LatLng(lat, lng)
		
		//map.panTo(event.latLng);
		
		//이전 마커가 존재하면 삭제
		if (m != null) {
			m.setMap(null);
		}
		b n
		m = new kakao.maps.Marker({
			position: event.latLng
		});
		
		m.setMap(map);
		
		
	});
	

	
	</script>
</body>
</html>