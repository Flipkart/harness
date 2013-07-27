function showBigImage(image)
{
    $('#block').show();
    var src = $(image).attr('src');
    var element = document.createElement("img");
    element.setAttribute("id", "bigimage");
    element.setAttribute("src", src);
    element.setAttribute("width", "598");
    element.setAttribute("height", "598");
    var div = document.getElementById("block");
    div.appendChild(element);
}

function hideBigImage()
{
    var parent = document.getElementById("block");
    while(parent.hasChildNodes())
        parent.removeChild( parent.childNodes[0] );
    $('#block').hide();
}

function openDetailReport(method)
{
    var id = $(method).attr("href");
    id = id.substring(1);
    var collap = document.getElementById(id);
    var id = $(collap).attr("id");
    collap.querySelector(".sign").innerHTML = "-";
    id = id.replace("m", "c");
    var element = document.getElementById(id);
    document.getElementById(id).style.height = 'auto';
    $(element).css("display", "block");
    $(collap).attr("onclick", "hideCollapsiable(this);");
    $(element).hide().slideDown('slow');
}

function showCollapsiable(collap)
{
    var id = $(collap).attr("id");
    collap.querySelector(".sign").innerHTML = "-";
    id = id.replace("m", "c");
    var element = document.getElementById(id);
    document.getElementById(id).style.height = 'auto';
    $(element).css("display", "block");
    $(collap).attr("onclick", "hideCollapsiable(this);");
    $(element).hide().slideDown('slow');
}

function hideCollapsiable(collap)
{
    var id = $(collap).attr("id");
    collap.querySelector(".sign").innerHTML = "+";
    id = id.replace("m", "c");
    var element = document.getElementById(id);
    document.getElementById(id).style.height = 0+'px';
    $(collap).attr("onclick", "showCollapsiable(this);");
    $(element).slideUp(170);
}