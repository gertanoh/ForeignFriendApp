

/*LatLng student1 = new LatLng(59.346098, 18.072738);
        LatLng student2 = new LatLng(59.347970, 18.068914);
        LatLng student3 = new LatLng(59.349006, 18.074619);
        LatLng student4 = new LatLng(59.346477, 18.076880);
        Marker marker1 = mMap.addMarker(new MarkerOptions().position(student1).title("Student_1"));
        Marker marker2 = mMap.addMarker(new MarkerOptions().position(student2).title("Student_2"));
        Marker marker3 = mMap.addMarker(new MarkerOptions().position(student3).title("Student_3"));
        Marker marker4 = mMap.addMarker(new MarkerOptions().position(student4).title("Student_4"));*/





//Retrieves locations added to the matching users depending on queries
DatabaseReference matchingUsers = database.getReference("locations" + userId);
        matchingUsers.addChildEventListener(new ChildEventListener() {

@Override
public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Marker m = mMap.addMarker(new MarkerOptions().position(parseLatLng(dataSnapshot)).title(dataSnapshot.getKey()));
        existingMarkers.put(dataSnapshot.getKey(), m);

        }

@Override
public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Marker m = existingMarkers.get(dataSnapshot.getKey());
        m.setPosition(parseLatLng(dataSnapshot));
        }

@Override
public void onChildRemoved(DataSnapshot dataSnapshot) {
        Marker m = existingMarkers.get(dataSnapshot.getKey());
        m.remove();
        existingMarkers.remove(dataSnapshot.getKey());

        }

@Override
public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

@Override
public void onCancelled(DatabaseError databaseError) {

        }
public LatLng parseLatLng(DataSnapshot dataSnapShot){
        String[] latlong = dataSnapShot.getValue().toString().split(",");
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);
        LatLng latlng = new LatLng(latitude, longitude);
        return latlng;
        }
        });

public void updateLocation(LatLng latLng){
final String loc = getCurrentLocation(); //TODO transform latlng to String loc
final DatabaseReference refUserMatch = database.getReference("locations/");
        DatabaseReference refMyLocation = database.getReference("users/"+ userId + "/location");

        refMyLocation.setValue(loc); //updates the value of my current location inside users/Me
        refMyLocation = database.getReference("locations" + userId);
        refMyLocation.addListenerForSingleValueEvent(new ValueEventListener(){

@Override
public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot userMatch: dataSnapshot.getChildren()) {
        refUserMatch.child(userMatch.getKey() + "/" + userId).setValue(loc); //updates the value of the location of current user in the list of "contacts" of every other user which matches
        }

        }

@Override
public void onCancelled(DatabaseError databaseError) {

        }
        });


        }