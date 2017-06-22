//Este comando acrescenta a class 'alternarmenu' na barra lateral e no conteúdo, 
//onde ao clicar no icone do topo alterna (mostra ou oculta) a barra lateral e o 
//menu e ajusta (extende) o conteudo na pagina
$(document).ready(function(){
   
    $('.js-alternarmenu').bind('click', function(){
        
        $('.js-barralateral').toggleClass('alternarmenu');
        $('.js-conteudo').toggleClass('alternarmenu');
        $('.js-footer').toggleClass('alternarmenu');
        
    });
});
