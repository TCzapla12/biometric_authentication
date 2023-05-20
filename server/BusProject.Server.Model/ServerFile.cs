namespace BusProject.Server.Model
{
    using System;
    public class ServerFile : Object
    {
        public string Name { get; private set; }
        public string Type { get; private set; }
        public double Size { get; private set; }
        public DateTime Date { get; private set; }
        public ServerFile(string id, double size, DateTime date): base(id) 
        {
            (this.Name, this.Type, this.Size, this.Date) = (id.Split('.')[0], id.Split('.')[1], size, date);
        }
        public ServerFile(string id): base(id) { }
    }
}
