package pl.javastart.library.model;

import pl.javastart.library.Exceptions.PublicationArleadyExistException;
import pl.javastart.library.Exceptions.UserArleadyExistsException;

import java.io.Serializable;

import java.util.*;

public class Library  implements Serializable {

    private Map<String,Publication> publications=new HashMap<>();
    private Map<String,LibraryUser> users=new HashMap<>();

    public Collection<Publication> getSortedPublications(Comparator<Publication> comparator){
        ArrayList<Publication> list = new ArrayList<>(publications.values());
        list.sort(comparator);
        return list;

    }
    public Optional<Publication> findPublicationByTitle(String title){
        return Optional.ofNullable(publications.get(title));
    }




    public Map<String, Publication> getPublications() {
        return publications;
    }

    public Map<String, LibraryUser> getUsers() {
        return users;
    }

    public Collection<LibraryUser> getSortedUsers(Comparator<LibraryUser> comparator){
    ArrayList<LibraryUser> userList = new ArrayList<>(users.values());
    userList.sort(comparator);
    return userList;
    }

    public void addPublication(Publication publication){
        if(publications.containsKey(publication.getTitle())){
            throw new PublicationArleadyExistException("Publikacja o takim tytule już istnieje "+publication.getTitle());
        }
       publications.put(publication.getTitle(),publication);

    }
public void addUser(LibraryUser user){
        if(users.containsKey(user.getPesel())){
            throw new UserArleadyExistsException("Użytkownik ze wskazanym peselem już istnieje "+user.getPesel());
    }
        users.put(user.getPesel(),user);
}
    public boolean removePublication(Publication publication){
        if(publications.containsValue(publication)){
            publications.remove(publication.getTitle());
            return true;
        }else{
            return false;
        }

    }
}

