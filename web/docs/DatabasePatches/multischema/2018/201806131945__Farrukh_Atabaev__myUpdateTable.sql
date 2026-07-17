update "anv".myupdate mu set typecode=(select mut.code from myupdatetype mut where mu.updatetypeid=mut.id);

alter table "0".myupdate drop column if exists updatetypeid;

alter table "anv".myupdate drop column if exists updatetypeid;