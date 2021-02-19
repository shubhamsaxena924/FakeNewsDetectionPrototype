package parser;

import java.util.List;
import java.util.Objects;

public class HeadAndRef {
    //int page number according to page ranking of each site
    private int num;
    //String for search result heading
    private String head;
    //String for its reference/link
    private String ref;
    //List of image links
    private List imageLinks;

    public HeadAndRef(int num, String head, String ref, List imageLinks) {
        this.num = num;
        this.head = head;
        this.ref = ref;
        this.imageLinks = imageLinks;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(List imageLinks) {
        this.imageLinks = imageLinks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeadAndRef that = (HeadAndRef) o;
        return getNum() == that.getNum() && Objects.equals(getHead(), that.getHead()) && Objects.equals(getRef(), that.getRef()) && Objects.equals(getImageLinks(), that.getImageLinks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNum(), getHead(), getRef(), getImageLinks());
    }

    @Override
    public String toString() {
        return head + " : " + ref;
    }

    //Gives String Array Representation of the Object
    public String[] toStringArray() {
        String[] arr = {String.valueOf(num), head, ref, String.valueOf(imageLinks)};
        return arr;
    }
}
