// 遍历object方法 展示node的属性信息
function subObj(snode,id) {
    var nodearr;

    if (snode.attribute != undefined) {
        nodearr = snode.attribute;
    } else {
        nodearr = null;
    }
    // $.each($.parseJSON(node.attribute), function(key, val) {
    // $.each(nodearr, function(key, val) {
    $("#"+id).find(":input").each(function(i, k) {
        if ($(k).attr("type") != "button" && $(k).attr("type") != "radio") {
            $(k).val("");
        }
    });

    if (nodearr != null)
        $.each(nodearr, function(key, val) {
            if ($.isPlainObject(val) || $.isArray(val)) {
                subObj(val);
            } else {

                // console.log(key + "===" + val);

                // $("#" + id).show();
                $("#"+id).attr("data-node", snode.nodeid);
                $("#"+id).find(":input[name='" + key + "']").each(function(i, k) {
                    if ($(k).attr("type") != "button" && $(k).attr("type") != "radio") {
                        if (key == "command") {
                            val = val.replace(/\;/g, " ");
                        }
                        $(k).val(val);
                    } else if ($(k).attr("type") == "radio") {
                        if ($(k).val() == val) {
                            $(k).attr("checked", 'checked');
                        }
                    }
                });
            }
        });
}

function changeIco(){
    $(".forwardCard").toggleClass('fa-chevron-down');
}