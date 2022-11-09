import { Component, Vue, Inject } from 'vue-property-decorator';

import { IEmpresa } from '@/shared/model/empresa.model';
import EmpresaService from './empresa.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class EmpresaDetails extends Vue {
  @Inject('empresaService') private empresaService: () => EmpresaService;
  @Inject('alertService') private alertService: () => AlertService;

  public empresa: IEmpresa = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.empresaId) {
        vm.retrieveEmpresa(to.params.empresaId);
      }
    });
  }

  public retrieveEmpresa(empresaId) {
    this.empresaService()
      .find(empresaId)
      .then(res => {
        this.empresa = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
