    $(function() {
        $('#input-search').on('keyup', function() {
          var rex = new RegExp($(this).val(), 'i');
            $('.searchable-container .items').hide();
            $('.searchable-container .items').filter(function() {
                return rex.test($(this).text());
            }).show();
        });
    });

    function markAsDone(index){
        document.getElementById(index.toString()).value = "1";
        for(i =0; i< 4 ; i++){
        console.log(document.getElementById(i))}
        console.log( document.getElementById(index.toString()));
    }
    function markAsUnDone(index){
        document.getElementById(index.toString()).value = "0";
                console.log( document.getElementById(index.toString()));
                        for(i =0; i< 4 ; i++){
                        console.log(document.getElementById(i))}
    }
