import os
import random
import numpy as np
import pandas as pd

os.makedirs('datasets', exist_ok=True)
df = pd.read_csv('orders.csv')
produtos = df['product_name'].unique().tolist()

tamanhos = [10000, 30000, 50000, 70000, 90000, 110000, 130000, 150000]

# DATASET 1: Expansão da base real mantendo a distribuição original
print('A gerar Dataset 1 (Base E-commerce Expandida)...')
dfs_exp = []
for i in range(6):  # 6 x 25.000 = 150.000 linhas
  temp = df[['order_id', 'customer_id', 'product_name']].copy()
  # Cria novos IDs de clientes em cada bloco para escalar o número de cestas proporcionalmente
  temp['customer_id'] = temp['customer_id'] + f'_B{i}'
  dfs_exp.append(temp)

df_full_1 = pd.concat(dfs_exp, ignore_index=True)
for t in tamanhos:
  df_full_1.iloc[:t].to_csv(f'datasets/ds1_{t}.csv', index=False)

# DATASET 2: Sintético com distribuição Zipf (Pareto: alguns produtos muito frequentes)
print('A gerar Dataset 2 (Sintético - Distribuição Assimétrica/Pareto)...')
np.random.seed(42)
pesos = np.random.zipf(1.6, len(produtos))
pesos = pesos / pesos.sum()

n_max = 150000
clientes_ds2 = [
    f'CUST_{random.randint(1, 25000)}' for _ in range(n_max)
]  # Cestas mais densas (~6 itens/cliente)
prods_ds2 = np.random.choice(produtos, size=n_max, p=pesos)

df_full_2 = pd.DataFrame({
    'order_id': [f'ORD_{i}' for i in range(n_max)],
    'customer_id': clientes_ds2,
    'product_name': prods_ds2,
})

for t in tamanhos:
  df_full_2.iloc[:t].to_csv(f'datasets/ds2_{t}.csv', index=False)

print("Concluído! Todos os ficheiros foram gerados na pasta 'datasets/'.")