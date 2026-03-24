import { defineStore } from 'pinia'

export const useCustomerStore = defineStore('customer', {
  state: () => ({
    customerList: [] as any[],
    currentCustomer: null as any,
    loading: false
  }),
  actions: {
    setCustomerList(list: any[]) {
      this.customerList = list
    },
    setCurrentCustomer(customer: any) {
      this.currentCustomer = customer
    },
    setLoading(loading: boolean) {
      this.loading = loading
    }
  }
})