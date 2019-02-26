public static Double distance(Double lat1, Double lng1, Double lat2, Double lng2) {

       int R = 6378137;   /* Earth's mean radius in meter */
       double dLat = rad(lat1 -lat2);
       double dLong = rad(lng1-lng2);
       double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(rad(lat1)) * Math.cos(rad(lat2)) * Math.sin(dLong / 2) * Math.sin(dLong / 2);
       double c = 2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
       return R*c;
   }
