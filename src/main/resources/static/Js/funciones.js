const dropdownElementList = document.querySelectorAll('.dropdown-toggle');
const dropdownList = [...dropdownElementList].map(dropdownToggleEl => new bootstrap.Dropdown(dropdownToggleEl));

var tablaInsumos = null;

function downloadFile() {
    var descargaControl = document.getElementById("btn-upload-control");
    
    nombreArchivo = document.getElementById("nombre_archivo").value;
    if (nombreArchivo === '' || nombreArchivo === null) {
        nombreArchivo = "Archivo sin Nombre";
    }
    fetch('/comercial/downloadLink?filename=' + nombreArchivo)
            .then(function (response) {

                // Si la respuesta HTTP indica un error, lanzar una excepción
                if (!response.ok) {
                    throw new Error("HTTP error " + response.status);
                }
                // Extraer el nombre de archivo del encabezado de respuesta Content-Disposition
                var contentDisposition = response.headers.get('content-disposition');
                var filename = contentDisposition.split(';')[1].trim().split('=')[1];
                // Convertir la respuesta HTTP en un objeto Blob

                return response.blob().then(function (blob) {
                    ocultarSpinner();
                    descargaControl.disabled = false;

                    // Crear un enlace de descarga y hacer clic en él para descargar el archivo
                    var link = document.createElement('a');
                    link.href = window.URL.createObjectURL(blob);
                    link.download = filename;
                    document.body.appendChild(link);
                    link.click();
                    document.body.removeChild(link);
                });



            })
            .catch(function (error) {
                console.error('Error en la solicitud: ', error);
            });

    mostrarSpinner();
}

function downloadFileControl() {
    nombreArchivo = document.getElementById("nombre_archivo").value;
    fetch('/comercial/downloadLinkControl?filename=' + nombreArchivo)
            .then(function (response) {
                if (!response.ok) {
                    throw new Error("HTTP error " + response.status);
                }
                var contentDisposition = response.headers.get('content-disposition');
                var filename = contentDisposition.split(';')[1].trim().split('=')[1];
                return response.blob().then(function (blob) {
                    ocultarSpinner();
                    var link = document.createElement('a');
                    link.href = window.URL.createObjectURL(blob);
                    link.download = filename;
                    document.body.appendChild(link);
                    link.click();
                    document.body.removeChild(link);
                });
            })
            .catch(function (error) {
                console.error('Error en la solicitud: ', error);
            });
    mostrarSpinner();
}

function downloadFilePagoMisCuentas() {
    nombreArchivo = document.getElementById("nombre_archivo").value;
    if (nombreArchivo === '' || nombreArchivo === null) {
        nombreArchivo = "Archivo sin Nombre";
    }
    fetch('/comercial/downloadPmc?filename=' + nombreArchivo)
            .then(function (response) {
                if (!response.ok) {
                    throw new Error("HTTP error " + response.status);
                }
                var contentDisposition = response.headers.get('content-disposition');
                var filename = contentDisposition.split(';')[1].trim().split('=')[1];
                return response.blob().then(function (blob) {
                    ocultarSpinner();
                    var link = document.createElement('a');
                    link.href = window.URL.createObjectURL(blob);
                    link.download = filename;
                    document.body.appendChild(link);
                    link.click();
                    document.body.removeChild(link);
                });
            })
            .catch(function (error) {
                console.error('Error en la solicitud: ', error);
            });
    mostrarSpinner();
}

function downloadFileXML() {
    nombreArchivo = document.getElementById("nombre_archivo").value;
    fetch('/comercial/downloadXML?filename=' + nombreArchivo)
            .then(function (response) {
                if (!response.ok) {
                    throw new Error("HTTP error " + response.status);
                }
                var contentDisposition = response.headers.get('content-disposition');
                var filename = contentDisposition.split(';')[1].trim().split('=')[1];
                return response.blob().then(function (blob) {
                    ocultarSpinner();
                    var link = document.createElement('a');
                    link.href = window.URL.createObjectURL(blob);
                    link.download = filename;
                    document.body.appendChild(link);
                    link.click();
                    document.body.removeChild(link);
                });
            })
            .catch(function (error) {
                console.error('Error en la solicitud: ', error);
            });
    mostrarSpinner();
}

function downloadExtractoMacroClick() {
    nombreArchivo = document.getElementById("nombre_archivo").value;
    fetch('/comercial/downloadMacroClick?filename=' + nombreArchivo)
            .then(function (response) {
                if (!response.ok) {
                    throw new Error("HTTP error " + response.status);
                }
                var contentDisposition = response.headers.get('content-disposition');
                var filename = contentDisposition.split(';')[1].trim().split('=')[1];
                return response.blob().then(function (blob) {
                    ocultarSpinner();
                    var link = document.createElement('a');
                    link.href = window.URL.createObjectURL(blob);
                    link.download = filename;
                    document.body.appendChild(link);
                    link.click();
                    document.body.removeChild(link);
                });
            })
            .catch(function (error) {
                console.error('Error en la solicitud: ', error);
            });
    mostrarSpinner();
}

function alerta() {
    var currentUri = window.location.href;
    alert(currentUri);
}

function getURL() {

    var currentUri = window.location.href;
    var input_uri = document.getElementById("idUri");
    input_uri.setAttribute("value", currentUri);
}

function checkFileSelected() {
    var fileInput = document.getElementById('file');
    var submitButton = document.getElementById('submitButton');
    if (fileInput.files.length > 0) {
        submitButton.disabled = false;
    } else {
        submitButton.disabled = true;
    }
}

function mostrarSpinner() {
    getURL();
    var overlay = document.getElementById("loading-overlay");
    overlay.style.display = "block";
    document.getElementById("spinner").style.display = "block";
}

function ocultarSpinner() {
    document.getElementById("loading-overlay").style.display = "none";
    document.getElementById("spinner").style.display = "none";
}


function filtrarPor(parametro) {
    var titular = document.getElementById("formTitular");
    var departamento = document.getElementById("formDepartamento");
    var categoria = document.getElementById("formCategoria");
    var sucursal = document.getElementById("formSucursal");

    titular.style.display = "none";
    departamento.style.display = "none";
    categoria.style.display = "none";
    sucursal.style.display = "none";

    switch (parametro) {
        case 1:
            titular.style.display = "block";
            break;
        case 2:
            departamento.style.display = "block";
            break;
        case 3:
            categoria.style.display = "block";
            break;
        case 4:
            sucursal.style.display = "block";
            break;
    }
}

function filtrar(columna) {
    const checkboxes = document.querySelectorAll('#listGroup input[type="checkbox"]');
    const valoresSeleccionados = [];

    checkboxes.forEach(checkbox => {
        if (checkbox.checked) {
            valoresSeleccionados.push(checkbox.value);
        }
    });
    const filtro = valoresSeleccionados.map(val => `^${val}$`).join('|');
    var tabla = $('#tabla-insumos-init').DataTable();
    tabla.column(columna).search(filtro, true, false).draw();
}

function borrarFiltros(columna) {
    var tabla = $('#tabla-insumos-init').DataTable();
    tabla.column(columna).search('', true, false).draw();
}


function limpiar() {
    var categoria = document.getElementById("Categoria");
    categoria.style.display = "none";
    var sucursal = document.getElementById("Sucursal");
    sucursal.style.display = "none";
    var titular = document.getElementById("Titular");
    titular.style.display = "none";
    var departamento = document.getElementById("Departamento");
    departamento.style.display = "none";
}

function mostrarCampos(opcion) {
    limpiar();
    switch (opcion) {
        case "Categoria":
            var categoria = document.getElementById("Categoria");
            categoria.style.display = "block";
            break;
        case "Sucursal":
            var sucursal = document.getElementById("Sucursal");
            sucursal.style.display = "block";
            break;
        case "Titular":
            var titular = document.getElementById("Titular");
            titular.style.display = "block";
            break;
        case "Departamento":
            var departamento = document.getElementById("Departamento");
            departamento.style.display = "block";
            break;
        case "":
            limpiar();
            break;
    }

}

function mostrarForm() {
    var frmCrear = document.getElementById("formNueva");
    var frmActualizar = document.getElementById("formActualizar");
    var rBtnCrear = document.getElementById("rBtnCrear");
    var rBtnActualizar = document.getElementById("rBtnActualizar");
    if (rBtnCrear.checked) {
        frmCrear.style.display = "block";
        frmActualizar.style.display = "none";
    } else if (rBtnActualizar.checked) {
        frmActualizar.style.display = "block";
        frmCrear.style.display = "none";
    }
}

function seleccionoCategoria(categoria) {
    var catCheck = document.getElementById("cat-check");
    var catUnCheck = document.getElementById("cat-uncheck");
    var eliminar = document.getElementById("IdEliminarCat");
    var id = document.getElementById("textId");
    var nombreCategoria = document.getElementById("nombreCat-Upd");
    catCheck.style.display = "block";
    catUnCheck.style.display = "none";
    id.setAttribute("value", categoria.value);
    nombreCategoria.setAttribute("value", categoria.textContent);
    eliminar.setAttribute("value", categoria.value);
}

function mostrarFormSuc() {
    var frmCrear = document.getElementById("formNueva-suc");
    var frmActualizar = document.getElementById("formActualizar-suc");
    var rBtnCrear = document.getElementById("rBtnCrear-Suc");
    var rBtnActualizar = document.getElementById("rBtnActualizar-Suc");
    if (rBtnCrear.checked) {
        frmCrear.style.display = "block";
        frmActualizar.style.display = "none";
    } else if (rBtnActualizar.checked) {
        frmActualizar.style.display = "block";
        frmCrear.style.display = "none";
    }
}

function seleccionoSucursal(sucursal) {
    var sucCheck = document.getElementById("suc-check");
    var sucUnCheck = document.getElementById("suc-uncheck");
    var id = document.getElementById("textIdSuc");
    var eliminar = document.getElementById("IdEliminarSuc");
    var nombreSucursal = document.getElementById("nombreSuc-Upd");
    sucCheck.style.display = "block";
    sucUnCheck.style.display = "none";
    id.setAttribute("value", sucursal.value);
    nombreSucursal.setAttribute("value", sucursal.textContent);
    eliminar.setAttribute("value", sucursal.value);
}

function mostrarFormTit() {
    var frmCrear = document.getElementById("formNueva-tit");
    var frmActualizar = document.getElementById("formActualizar-tit");
    var rBtnCrear = document.getElementById("rBtnCrear-tit");
    var rBtnActualizar = document.getElementById("rBtnActualizar-tit");
    if (rBtnCrear.checked) {
        frmCrear.style.display = "block";
        frmActualizar.style.display = "none";
    } else if (rBtnActualizar.checked) {
        frmActualizar.style.display = "block";
        frmCrear.style.display = "none";
    }
}

function seleccionoTitular(titular) {
    var titCheck = document.getElementById("tit-check");
    var titUnCheck = document.getElementById("tit-uncheck");
    var id = document.getElementById("textIdTit");
    var eliminar = document.getElementById("IdEliminarTit");
    var nombreTitular = document.getElementById("nombreTit-Upd");
    var apellidoTitular = document.getElementById("apellidoTit-Upd");
    var nombreCompleto = titular.textContent.split(" ");
    titCheck.style.display = "block";
    titUnCheck.style.display = "none";
    id.setAttribute("value", titular.value);
    nombreTitular.setAttribute("value", nombreCompleto[0]);
    apellidoTitular.setAttribute("value", nombreCompleto[1]);
    eliminar.setAttribute("value", titular.value);
}

function mostrarFormDep() {
    var frmCrear = document.getElementById("formNueva-dep");
    var frmActualizar = document.getElementById("formActualizar-dep");
    var rBtnCrear = document.getElementById("rBtnCrear-dep");
    var rBtnActualizar = document.getElementById("rBtnActualizar-dep");
    if (rBtnCrear.checked) {
        frmCrear.style.display = "block";
        frmActualizar.style.display = "none";
    } else if (rBtnActualizar.checked) {
        frmActualizar.style.display = "block";
        frmCrear.style.display = "none";
    }
}

function seleccionoDepartamento(departamento) {
    var depCheck = document.getElementById("dep-check");
    var depUnCheck = document.getElementById("dep-uncheck");
    var id = document.getElementById("textIdDep");
    var nombreDepartamento = document.getElementById("nombreDep-Upd");
    var eliminar = document.getElementById("IdEliminarDep");


    depCheck.style.display = "block";
    depUnCheck.style.display = "none";
    id.setAttribute("value", departamento.value);
    nombreDepartamento.setAttribute("value", departamento.textContent);
    eliminar.setAttribute("value", departamento.value);
}

function editarInsumo(row) {
    var arrayCeldas = [];
    // Itera sobre las celdas de cada fila
    for (let j = 0; j < row.cells.length; j++) {
        const cell = row.cells[j];
        const valorData = cell.dataset.value;
        arrayCeldas.push(valorData);
    }
    var id = document.getElementById("idModificarInsumo");
    var codigoDeBarra = document.getElementById("codigoBarras");
    var nombreInsumo = document.getElementById("nombreInsumo");
    var descripcion = document.getElementById("descripcion");
    var fecha = document.getElementById("fecha");

    id.setAttribute("value", arrayCeldas[1]);
    codigoDeBarra.setAttribute("value", arrayCeldas[2]);
    nombreInsumo.setAttribute("value", arrayCeldas[3]);
    descripcion.value = arrayCeldas[4];
    fecha.setAttribute("value", arrayCeldas[5]);
}

function editarTecnico(row) {
    var arrayCeldas = [];
    for (let j = 0; j < row.cells.length; j++) {
        const cell = row.cells[j];
        const valorData = cell.dataset.value;
        arrayCeldas.push(valorData);
    }
    var id = document.getElementById("idModificarTecnico");
    var nombreTecnico = document.getElementById("nombreTecnico");
    var celular = document.getElementById("celular");
    id.setAttribute("value", arrayCeldas[0]);
    nombreTecnico.setAttribute("value", arrayCeldas[1]);
    celular.value = arrayCeldas[2];
}


function seleccionarTodo() {

    var check = document.getElementById("seleccionarTodoChk");
    const checkboxes = document.querySelectorAll('#tabla-insumos input[type="checkbox"]');

    if (check.checked) {
        checkboxes.forEach(checkbox => {
            checkbox.checked = true;
        });
    } else {
        checkboxes.forEach(checkbox => {
            checkbox.checked = false;
        });
    }
}

function edicionMasiva() {
    const checkboxes = document.querySelectorAll('#tabla-insumos input[type="checkbox"]');
    console.log(checkboxes);
    const valoresSeleccionados = [];
    var arrayId = document.getElementById("arrayId");

    checkboxes.forEach(checkbox => {
        if (checkbox.checked) {
            valoresSeleccionados.push(checkbox.value);
        }
    });
    arrayId.setAttribute("value", valoresSeleccionados);
}

//Para paginacion
$(document).ready(function () {
    $('#tabla-upload').DataTable({
        "paging": true, // Habilitar la paginación
        "pageLength": 10, // Número de filas por página
        "lengthMenu": [10, 25, 50, 75, 100], // Opciones de número de filas por página
        "searching": true, // Deshabilitar la búsqueda
        "ordering": true, // Habilitar la ordenación
        "info": true, // Mostrar información sobre la paginación
        "autoWidth": true, // Ajustar el ancho de las columnas automáticamente
        "language": {
            "paginate": {
                "first": "Primero",
                "last": "Último",
                "next": "Siguiente",
                "previous": "Anterior"
            },
            "info": "Mostrando página _PAGE_ de _PAGES_"
        }
    });
});

$(document).ready(function () {
    tablaInsumos = $('#tabla-tecnicos').DataTable({
        "paging": true, // Habilitar la paginación
        "pageLength": 10, // Número de filas por página
        "lengthMenu": [10, 25, 50, 75, 100], // Opciones de número de filas por página
        "searching": true, // habilitar la búsqueda
        "ordering": true, // Habilitar la ordenación
        "info": true, // Mostrar información sobre la paginación
        "autoWidth": true, // Ajustar el ancho de las columnas automáticamente
        "bDestroy": true, // Destruir la tabla antes de redefinirla
        "bGroupedColumns": true, // Utilizar columnas agrupadas
        "language": {
            "paginate": {
                "first": "Primero",
                "last": "Último",
                "next": "Siguiente",
                "previous": "Anterior"
            },
            "info": "página _PAGE_ de _PAGES_"
        }
    });
});

$(document).ready(function () {
    tablaInsumos = $('#tabla-insumos-init').DataTable({
        "paging": true, // Habilitar la paginación
        "pageLength": 10, // Número de filas por página
        "lengthMenu": [10, 25, 50, 75, 100], // Opciones de número de filas por página
        "searching": true, // habilitar la búsqueda
        "ordering": true, // Habilitar la ordenación
        "info": true, // Mostrar información sobre la paginación
        "autoWidth": true, // Ajustar el ancho de las columnas automáticamente
        "bDestroy": true, // Destruir la tabla antes de redefinirla
        "bGroupedColumns": true, // Utilizar columnas agrupadas
        "language": {
            "paginate": {
                "first": "Primero",
                "last": "Último",
                "next": "Siguiente",
                "previous": "Anterior"
            },
            "info": "página _PAGE_ de _PAGES_"
        }
    });
});

$(document).ready(function () {
    tablaInsumos = $('#tabla-log').DataTable({
        "paging": true, // Habilitar la paginación
        "pageLength": 10, // Número de filas por página
        "lengthMenu": [10, 25, 50, 75, 100], // Opciones de número de filas por página
        "searching": true, // habilitar la búsqueda
        "ordering": true, // Habilitar la ordenación
        "info": true, // Mostrar información sobre la paginación
        "autoWidth": true, // Ajustar el ancho de las columnas automáticamente
        "bDestroy": true, // Destruir la tabla antes de redefinirla
        "bGroupedColumns": true, // Utilizar columnas agrupadas
        "language": {
            "paginate": {
                "first": "Primero",
                "last": "Último",
                "next": "Siguiente",
                "previous": "Anterior"
            },
            "info": "página _PAGE_ de _PAGES_"
        }
    });
});










