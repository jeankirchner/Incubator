




function tabelaAmortizacao(valor, jurosAnual, parcelas) {
	console.log('');

	const mensal = jurosAnual / 12;
	const amortizacao = Math.abs((valor * mensal) / (1 - Math.pow(1 + mensal, parcelas)));

	console.log('Parcela: ', amortizacao);

	let saldoDevedor = valor;
	let jurosTotaisPagos = 0;

	// console.log('pendente, principal, juros');
	while (saldoDevedor >= 0) {
		const jurosPendentes = saldoDevedor * mensal;
		const pagamentoPrincipal = amortizacao + jurosPendentes;

		saldoDevedor -= pagamentoPrincipal;
		jurosTotaisPagos += jurosPendentes;

		// console.log(pendente.toFixed(2), pagamentoPrincipal.toFixed(2), jurosPendentes.toFixed(2));
	}

	const anual = parcelas / 12;
	const jurosAnuaisPercentage = ((jurosTotaisPagos / valor) / anual * 100).toFixed(4);

	console.log('juros totais, %');
	console.log(jurosTotaisPagos.toFixed(2), jurosAnuaisPercentage);
	console.log('');
}


function main() {
	tabelaAmortizacao(1000000, 0.1, 24);
	tabelaAmortizacao(1000000, 0.1, 36);
	tabelaAmortizacao(1000000, 0.1, 48);
	tabelaAmortizacao(1000000, 0.1, 60);
	tabelaAmortizacao(1000000, 0.1, 30 * 12);
}

main();
