import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import { IArl, Arl } from '@/shared/model/arl.model';
import ArlService from './arl.service';

const validations: any = {
  arl: {
    nombre: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class ArlUpdate extends Vue {
  @Inject('arlService') private arlService: () => ArlService;
  @Inject('alertService') private alertService: () => AlertService;

  public arl: IArl = new Arl();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.arlId) {
        vm.retrieveArl(to.params.arlId);
      }
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.arl.id) {
      this.arlService()
        .update(this.arl)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Arl is updated with identifier ' + param.id;
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.arlService()
        .create(this.arl)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Arl is created with identifier ' + param.id;
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrieveArl(arlId): void {
    this.arlService()
      .find(arlId)
      .then(res => {
        this.arl = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
