$(document).ready(function() {

    tableAplicaciones = $('#contadorPeticionesList').dataTable({
        "language" : {
            "sProcessing" : "Procesando...",
            "sLengthMenu" : "Mostrar _MENU_",
            "sZeroRecords" : "No se encontraron resultados",
            "sEmptyTable" : "Ningún dato disponible en esta tabla",
            "sInfo" : "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
            "sInfoEmpty" : "Mostrando registros del 0 al 0 de un total de 0 registros",
            "sInfoFiltered" : "(filtrado de un total de _MAX_ registros)",
            "sInfoPostFix" : "",
            "sSearch" : "Buscar",
            "sUrl" : "",
            "sInfoThousands" : ",",
            "sLoadingRecords" : "Cargando...",
            "oPaginate" : {
                "sFirst" : "Primero",
                "sLast" : "Último",
                "sNext" : "Siguiente",
                "sPrevious" : "Anterior"
            },
            "oAria" : {
                "sSortAscending" : ": Activar para ordenar la columna de manera ascendente",
                "sSortDescending" : ": Activar para ordenar la columna de manera descendente"
            }
        },
        "lengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "Todo" ] ],
        "pageLength" : 25,
        "dom" : '<"top fld dsp_ib" f><"fld dsp_ib" l>t<"bottom" ir><"clear">',
        "bSort" : true,
        "aoColumns" : [ {
            "bSortable" : true,
            "orderSequence" : [ "desc", "asc" ],
            "width" : "40%"
        }, {
            "bSortable" : true,
            "orderSequence" : [ "desc", "asc" ],
            "width" : "40%"
        }, {
            "bSortable" : true,
            "orderSequence" : [ "desc", "asc" ],
            "width" : "20%"
        } ],
        "paging" : true,
        "order" : [ [ 0, "asc" ] ]
    });

});