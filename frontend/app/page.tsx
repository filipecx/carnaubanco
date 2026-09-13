"use client"

import Image from "next/image";
import BallanceCard from "./components/BallanceCard";
import UserCard from "./components/UserCard";
import { useEffect, useState } from "react";
import TransferForm from "./components/TransferForm";

import { ToastContainer } from "react-toastify";
import 'react-toastify/ReactToastify.css'

import { toast } from "react-toastify";

export default function Home() {

  interface User {
    id: string,
    name: string,
    email: string,
    cpf: string
  }

  interface Wallet {
    id: string,
    userId: string,
    currency: string, 
    balance: number,
    status: string
  }


  const [users, setUsers] = useState<User[] | null>([])
  const [sourceUser, setSourceUser] = useState<User | null>(null)
  const [selectedWalletId, setSelectedWalletId] = useState('')
  const [wallet, setWallet] = useState<Wallet | null>(null)
  const [targetWallet, setTargetWallet] = useState<Wallet | null>(null)
  const [email, setEmail] = useState<string>('')
  const [amount, setAmount] = useState<number | null>(null)
  const [loading, setLoading] = useState(false)

  const USERS_URL = `http://localhost:8081/api/v1/users`
  const WALLET_URL = `http://localhost:8082/api/v1/wallets`
  const fetchUsers = async () =>  {
    try {
      const res = await fetch(`${USERS_URL}`)
      const data = await res.json()
      setUsers(data)
      toast.success("Users carregados")
    } catch (err: any) {
      console.error(err)
    }
  }

  const handleTransfer = async (email: string) => {
    console.log(email)
    if (!email) {
      return
    }

    const idempotencyKey = crypto.randomUUID()
    const dicUsers: Record<string, string> = Object.fromEntries(users!.map(u => [u.email, u.id]))
    let userId = dicUsers[email]
    const targetWallet2 = await fetchWalletByEmail(userId)
    
    try {
      const res = await fetch(`${WALLET_URL}/transfer`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Idempotency-Key': idempotencyKey
        },
        body: JSON.stringify({
          sourceWalletId: wallet?.id,
          targetWalletId: targetWallet2.id,
          amount: amount
        })
      })
      setWallet((prevWallet) => ({...prevWallet, balance: prevWallet.balance - amount}))
      setTargetWallet((prevWallet) => ({...prevWallet, balance: prevWallet?.balance + amount}))
      const data = await res.json()

      if (!res.ok) {
        toast.error("Erro ao realizar transferência")
        throw new Error(data.message || 'Erro ao realizar transferência')
      }
    } catch (err: any) {
      console.error(err)
    }

  }

  const fetchWalletByEmail = async (userId: string) => {
    try {
      const res = await fetch(`${WALLET_URL}/by-user/${userId}`)
      if (!res.ok) {
        toast.error("Carteira não encontrada")
        throw new Error("Carteira não encontrada")
      }
      const data = await res.json()
      setTargetWallet(data)
      return data

    } catch (err: any) {
      console.error(err)
    }
  }


  const fetchWallet = async (userId: string) => {
    if (!userId) {
      return
    }
    setLoading(true)

    try {
      const res = await fetch(`${WALLET_URL}/by-user/${userId}`)
      if (!res.ok) {
        
        throw new Error("Carteira não encontrada")
      }
      const data = await res.json()
      setWallet(data)
    } catch (err: any) {
      console.error(err)
      setWallet(null)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchUsers()
  }, [selectedWalletId])

  return (
    <main className="min-h-screen bg-slate-900 text-slate-100 p-6 md:p-12">
      <div className="max-w-4xl mx-auto space-y-8">
        <header className="border-b border-slate-800 pb-6 flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
          <div className="flex items-center gap-3">
            <Image 
              src="/logo2.png"
              alt="Logo image"
              width={36}
              height={36}
              className="rounded-lg"  
            />
            <div>
              <h1 className="text-3xl font-bold text-emerald-400">Carnaubanco</h1>
              <p className="text-slate-400 text-sm">Plataforma de economia social</p>
            </div>
            
          </div>
        </header>

        <div className="grid grid-cols-1 md:grid-cols-1 gap-6 border-b-2 pb-6">
          <div>
            <p className="text-slate-200 font-bold text-2xl">Selecione um usuário para ver seu saldo</p>

            <h2 className="text-xs uppercase tracking-wider text-slate-400 
                font-semibold">Usuários cadastrados
            </h2>
          </div>
          
          <div className="grid grid-cols-3 gap-6">
            {
              users && 
              users.map((u) => {
                return(
                  <UserCard 
                  key={u.cpf}
                  id={u.id}
                  nome={u.name}
                  email={u.email}
                  cpf={u.cpf}
                  fetchWallet={fetchWallet}
                  />
                )
              })
            }
          </div>
        </div>
        <div className="grid grid-cols-1 gap-3">
          {wallet? 
          <div className="flex justify-evenly">
            <div>
              <BallanceCard balance={wallet!.balance} status={wallet?.status} currency={wallet?.currency}/>
            </div>
          <div>
              <TransferForm email={email} amount={amount} setEmail={setEmail} setAmount={setAmount} handleTransfer={handleTransfer}/>
          </div>
          {
            targetWallet&&
            <div>
              <BallanceCard 
                balance={targetWallet.balance}
                status={targetWallet.status}
                currency={targetWallet.currency}
              />
            </div>
          }
          </div>         
          : <p>Selecione um usuário para exibir seu saldo</p>}
        </div>
      </div>
                    <ToastContainer position="top-right" autoClose={2000}/>

    </main>
  );
}
