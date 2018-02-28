var coms = document.getElementsByClassName("commit-links-cell table-list-cell");

for(var i =0;i<coms.length;i++){
    var commitId = coms[i].getElementsByTagName("button")[0].getAttribute('data-clipboard-text');
    console.log(commitId);
    var mybut = document.getElementsByClassName("btn btn-outline tooltipped tooltipped-sw")[0];
    var newbut = document.createElement("a");
    newbut.setAttribute("aria-label","Check for smells");
    newbut.setAttribute("class","btn btn-outline tooltipped tooltipped-sw");
    newbut.setAttribute("rel","nofollow");
    newbut.setAttribute("href","http://www.github.com/saketrule?cid="+commitId);
    newbut.innerHTML = "S";
    coms[i].appendChild(newbut);
}
