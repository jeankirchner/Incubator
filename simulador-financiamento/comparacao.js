const COMPLETO = true;

function pp(obj) {
	console.log(JSON.stringify(obj, undefined, 2));
}

function ppImovel(obj) {
	pp(obj);
	// pp({
	// 	liquido: obj.liquido,
	// 	parcelaMedia: obj.sac.valorMedioParcela
	// })
}

function moeda(valor) {
	return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

function taxaAnualParaMensal(taxaAnual) {
	return Math.pow(1 + taxaAnual, 1 / 12) - 1;
}


function tabelaSAC(options) {
	const { valor, taxa, parcelas } = options;
	const jurosMensais = taxaAnualParaMensal(taxa);
	const amortizacaoFixa = valor / parcelas;
	const pagamentos = [];
	let saldoDevedor = valor;
	let saldoPago = 0;
	let jurosTotais = 0;

	for (let i = 0; i < parcelas; i++) {
		const jurosParciais = saldoDevedor * jurosMensais;
		jurosTotais += jurosParciais;
		const parcelaTotal = jurosParciais + amortizacaoFixa;
		saldoPago += parcelaTotal;
		saldoDevedor -= amortizacaoFixa;

		pagamentos.push(parcelaTotal);
	}

	const jurosPorcento = jurosTotais / valor;

	return {
		valor: valor,
		taxa: Math.pow(1 + jurosMensais, 12) - 1,
		parcelas,
		valorMedioParcela: saldoPago / parcelas,
		total: saldoPago,
		jurosPagos: jurosTotais,
		jurosPagosPorcento: jurosPorcento,
		pagamentos
	};
}

function formatarTabelaSAC(tabela) {
	const { valor, taxa, parcelas, valorMedioParcela, total, jurosPagos, jurosPagosPorcento } = tabela;
	const jurosPorcentoAnual = jurosPagosPorcento / (parcelas / 12);

	return {
		valor: moeda(valor),
		'%': (taxa).toFixed(2),
		parcelas,
		valorMedioParcela: moeda(valorMedioParcela),
		total: moeda(total),
		juros: moeda(jurosPagos),
		'juros %': (jurosPagosPorcento).toFixed(2),
		'juros anual %': (jurosPorcentoAnual).toFixed(2),
		tabela
	};
}

function jurosCompostos(valor, juros, parcelas) {
	return valor * Math.pow(1 + juros, parcelas);
}

function calcularInvestimentoImovel(options) {
	const { valor, parcelas, taxaValorizacao } = options;
	const sac = tabelaSAC(options);
	const retornoInvestimentoMensais = taxaAnualParaMensal(taxaValorizacao);
	const valorImovel = jurosCompostos(valor, retornoInvestimentoMensais, parcelas);

	const taxaCompra = valor * 0.05;
	const taxaManutencao = jurosCompostos(valor, taxaAnualParaMensal(0.01), parcelas) - valor;

	const liquido = valorImovel - sac.total - taxaCompra - taxaManutencao;

	const resp = {
		liquido: moeda(liquido),
	};

	if (COMPLETO) {
		return {
			...resp,
			sac: formatarTabelaSAC(sac),
			investimento: {
				taxaValorizacao,
				custos: {
					burocracia: moeda(taxaCompra),
					manutencao: moeda(taxaManutencao)
				},
				imovelValorizado: moeda(valorImovel),
			}
		}
	}
	return resp;
}

function calcularRendaFixa(options) {
	const { valor, meses, taxa } = options;
	const taxaMensal = taxaAnualParaMensal(taxa);
	const total = jurosCompostos(valor, taxaMensal, meses);
	const juros = total - valor;

	return { valor, taxa, meses, total, juros };
}

function calcularRendaFixaAportes(options) {
	const { valor, taxa, meses, pagamentos } = options;

	const taxaMensal = taxaAnualParaMensal(taxa);

	let total = 0;
	let aportes = 0;
	for (let pag of pagamentos) {
		total *= (1 + taxaMensal);
		total += pag;
		aportes += pag;
	}

	return { valor, taxa, meses, total, juros: total - aportes };
}

function fixaFmt(options) {
	const { valor, taxa, meses, total, juros } = options;

	return { valor: moeda(valor), taxa: (taxa).toFixed(2), meses, total: moeda(total), juros: moeda(juros) }
}

function calcularAluguel(options) {
	const { valor, meses } = options;
	const taxaAluguel = 0.005;
	const aumentoAnual = taxaAluguel;
	const anos = meses / 12;
	const taxaAluguelAnualizada = Math.pow(1 + taxaAluguel, 12) - 1;

	let valorImovel = valor;
	let aluguelTotal = 0;

	for (let i = 0; i < anos; i++) {
		const aluguel = valorImovel * taxaAluguelAnualizada;
		aluguelTotal += aluguel;
		valorImovel *= (1 + aumentoAnual);
	}

	return {
		mensal: aluguelTotal / meses,
		total: aluguelTotal
	}

}

function aluguelFmt(options) {
	const { mensal, total } = options;
	return { mensal: moeda(mensal), total: moeda(total) };
}

function calcularInvestimentoRendaFixa(options) {
	const aluguel = calcularAluguel(options);
	const investimentoRendaFixa = options.pagamentos ? calcularRendaFixaAportes(options) : calcularRendaFixa(options);

	const liquido = investimentoRendaFixa.juros - aluguel.total;

	const resp = {
		liquido: moeda(liquido),
	};

	if (COMPLETO) {
		return {
			aluguel: aluguelFmt(aluguel),
			investimento: fixaFmt(investimentoRendaFixa),
			...resp
		};
	}
	return resp;
}

function main() {
	const ipca = (3.5 / 100);
	const ipca6 = ipca + (6 / 100);
	const ipca8 = ipca + (8 / 100);
	//const ipca8 = (15 / 100);

	let valorImovel = 600000;
	let meses = 30 * 12;
	let taxa = 0.1;
	let taxaValorizacao = 0.1;

	console.log('imovel', valorImovel, meses, taxa, taxaValorizacao);
	let imovel = calcularInvestimentoImovel({ valor: valorImovel, taxa, parcelas: meses, taxaValorizacao });
	ppImovel(imovel);

	console.log('ipca6', valorImovel, meses, ipca6);
	pp(calcularInvestimentoRendaFixa({ valor: valorImovel, meses, taxa: ipca6, pagamentos: imovel.sac.tabela.pagamentos }).liquido);

	console.log('ipca8', valorImovel, meses, ipca8);
	pp(calcularInvestimentoRendaFixa({ valor: valorImovel, meses, taxa: ipca8, pagamentos: imovel.sac.tabela.pagamentos }).liquido);

	// valorImovel = 1000000;
	// meses = 30 * 12;
	// taxa = 0.1;
	// taxaValorizacao = 0.07;
	//
	// console.log('imovel', valorImovel, meses, taxa, taxaValorizacao);
	// imovel = calcularInvestimentoImovel({ valor: valorImovel, taxa, parcelas: meses, taxaValorizacao });
	// pp(imovel.liquido);
	//
	// console.log('ipca6', valorImovel, meses, ipca6);
	// pp(calcularInvestimentoRendaFixa({ valor: valorImovel, meses, taxa: ipca6, pagamentos: imovel.sac.tabela.pagamentos }).liquido);
	//
	// console.log('ipca8', valorImovel, meses, ipca8);
	// pp(calcularInvestimentoRendaFixa({ valor: valorImovel, meses, taxa: ipca8, pagamentos: imovel.sac.tabela.pagamentos }).liquido);
}

main();

