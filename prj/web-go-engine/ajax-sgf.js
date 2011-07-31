
    $(document).ready(function(){

        $("#bt").click(function(){

            var url = $("#url").val();
            var a = $("#test");

            a.load(url);

        });

    });

